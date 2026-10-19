# ElderCare — Backend

CRM para la gestión de residentes de una residencia geriátrica. API REST construida con **Spring Boot** y **MySQL**, con autenticación mediante **JWT**. Proyecto final de *Full Stack Developer with Java* (Factoría F5 + Capgemini).

> Este repositorio contiene **únicamente el backend** (API REST). El frontend (Vue 3) vive en un repositorio aparte.

---

## Tabla de contenidos

1. [Descripción](#descripción)
2. [Funcionalidades](#funcionalidades)
3. [Stack tecnológico](#stack-tecnológico)
4. [Arquitectura](#arquitectura)
5. [Modelo de datos](#modelo-de-datos)
6. [Seguridad](#seguridad)
7. [Requisitos previos](#requisitos-previos)
8. [Configuración](#configuración)
9. [Puesta en marcha](#puesta-en-marcha)
10. [Endpoints de la API](#endpoints-de-la-api)
11. [Autenticación paso a paso](#autenticación-paso-a-paso)
12. [Datos de ejemplo (seeding)](#datos-de-ejemplo-seeding)
13. [Tests](#tests)
14. [Decisiones de diseño y alcance](#decisiones-de-diseño-y-alcance)
15. [Estructura del repositorio](#estructura-del-repositorio)
16. [Autor](#autor)

---

## Descripción

ElderCare digitaliza la gestión diaria de una residencia de mayores: el perfil de cada residente, la asignación de habitaciones (respetando su capacidad) y el historial de informes profesionales. Está pensado como un CRM sencillo con un único rol administrador, orientado a la dirección del centro y constituye la base de un proyecto mayor.

La API expone operaciones CRUD sobre **residentes** y **habitaciones**, un flujo de **asignación residente ↔ habitación** con control de aforo, y la creación y consulta de **informes** asociados a cada residente. Todo el acceso está protegido con autenticación basada en tokens JWT.

En esencia, se ha decidido desarrollar aquello por lo que varias empresas del sector han tenido que pagar con dos objetivos: 
 1.  Entender el coste real de su implementación.
 2.  Mejorar el servicio que se contrató.

---

## Funcionalidades

- **Autenticación JWT**: login con usuario/contraseña que devuelve un token firmado (HS512); renovación de token mediante cabecera `X-Refresh-Token`.
- **Gestión de residentes**: alta, consulta (individual y listado), edición y baja. Incluye datos personales, grado de dependencia, dirección anterior, contacto familiar y profesional de referencia.
- **Gestión de habitaciones**: alta, consulta, edición y baja. La capacidad y la ocupación se calculan automáticamente a partir del tipo de habitación y de los residentes asignados.
- **Asignación de habitación**: asignar y retirar un residente de una habitación, con una **regla de negocio** que impide superar la capacidad (devuelve `409 Conflict`).
- **Informes**: creación de informes de texto asociados a un residente (con autor y fecha automáticos) y consulta del historial por residente.
- **Manejo centralizado de errores**: respuestas de error consistentes (validación, recurso no encontrado, habitación completa).
- **Siembra de datos**: usuario administrador y habitaciones iniciales cargados automáticamente al arrancar.
- **Informes**: desarrollo bastante avanzado, pero no estaba listo para la presentación del MVP.

---

## Stack tecnológico

| Categoría | Tecnología |
|---|---|
| Lenguaje | Java 21 |
| Framework | Spring Boot 4 |
| Seguridad | Spring Security + JWT nativo (NimbusJwtEncoder/Decoder, HS512) |
| Persistencia | Spring Data JPA (Hibernate) |
| Base de datos | MySQL 8 (en contenedor Docker) |
| Build | Maven (con wrapper `mvnw`) |
| Utilidades | Lombok |
| Testing | JUnit 5, Mockito, AssertJ, Testcontainers |
| Contenedores | Docker / Docker Compose |

---

## Arquitectura

El proyecto sigue una organización **por entidad** (*package-by-entity*): cada dominio (residente, habitación, informe…) agrupa su entidad, DTOs, mapper, servicio, controlador y excepciones. Dentro de cada servicio se aplica el patrón **interfaz + implementación**.

Capas por petición:

```
Controller  →  Service (interfaz + impl)  →  Repository (Spring Data JPA)
     ↑               ↓
   DTOs    ←→      Mapper      ←→      Entity
```

- **Controller**: expone los endpoints REST y delega en el servicio.
- **Service**: contiene la lógica de negocio (p. ej. el control de aforo).
- **Mapper**: convierte entre entidades y DTOs (request/response), aislando el modelo de persistencia de la API pública.
- **Repository**: acceso a datos mediante Spring Data JPA.
- **DTOs**: `record` de Java para las peticiones y respuestas.

---

## Modelo de datos

### Entidades principales

- **User** — credenciales del administrador (email, contraseña BCrypt, rol).
- **Resident** — residente del centro.
  - Datos personales: foto (URL), nombre, apellidos, edad, grado de dependencia.
  - **Address** (Value Object embebido): calle, ciudad, código postal.
  - **ReferenceRelative** (Value Object embebido): nombre, parentesco, teléfono.
  - Profesional de referencia (texto).
  - Relación `@ManyToOne` con **Room**.
- **Room** — habitación.
  - Número (único), tipo.
  - Relación `@OneToMany` con **Resident**.
- **Report** — informe asociado a un residente (contenido, autor, fecha de creación).

### Enumerados

- **DependencyGrade**: `SIN_GRADO`, `GRADO_I` (leve), `GRADO_II` (moderada), `GRADO_III` (severa).
- **RoomType**: `INDIVIDUAL` (capacidad 1), `DOUBLE` (capacidad 2).

### Relaciones

```
User (1) ────────── administra ────────── (N) [toda la API]

Room (1) ─────────── aloja ─────────────── (N) Resident
Resident (1) ─────── tiene ──────────────── (N) Report
```

> La **capacidad** de una habitación deriva de su tipo; la **ocupación** se calcula contando los residentes asignados; `full` es `ocupación >= capacidad`. No se persisten, se computan al construir la respuesta.

---

## Seguridad

Autenticación sin estado (*stateless*) basada en **JWT**:

1. El cliente envía sus credenciales al endpoint de **login** mediante *HTTP Basic*.
2. Spring Security valida contra la base de datos (`JpaUserDetailsService` + BCrypt).
3. El servidor responde con un **token JWT** firmado con HS512 (emisor `eldercare`, caducidad 15 minutos).
4. El cliente incluye ese token en cada petición protegida: `Authorization: Bearer <token>`.
5. Un filtro de refresco (`TokenRefreshFilter`) puede devolver un token renovado en la cabecera `X-Refresh-Token` para prolongar la sesión sin re-login.

Detalles:

- Contraseñas cifradas con **BCrypt**.
- CSRF deshabilitado (API stateless).
- CORS configurado para el frontend en `http://localhost:5173`.
- Un único rol: **ADMIN**.

---

## Requisitos previos

- **Java 21** (JDK)
- **Maven** (o usar el wrapper `./mvnw` incluido)
- **Docker Desktop** en marcha (para MySQL y para los tests con Testcontainers)

---

## Configuración

La configuración se reparte en perfiles de Spring y variables de entorno.

### Variables de entorno

Crea un archivo `.env` en la raíz a partir de la plantilla:

```bash
cp .env.example .env
```

Variables principales (ajústalas a tu entorno):

```properties
MYSQL_HOST=localhost
# credenciales de la BD, etc.
```

> El `.env` **no se versiona** (está en `.gitignore`); el `.env.example` sí, como documentación de qué variables hacen falta.

### Perfiles

- `application.properties` — configuración común.
- `application-mysql.properties` — conexión a MySQL (`db_p5`), `ddl-auto`, inicialización de datos.

### Base de datos (Docker Compose)

El `docker-compose.yml` levanta el contenedor de MySQL. Gracias a `spring-boot-docker-compose`, **Spring arranca el contenedor automáticamente** al iniciar la aplicación si Docker está en marcha.

---

## Puesta en marcha

1. Arranca **Docker Desktop**.
2. Clona el repositorio y sitúate en su carpeta:

```bash
   git clone <URL_DEL_REPOSITORIO>
   cd eldercare-backend
```

3. Crea tu `.env` (ver [Configuración](#configuración)).
4. Arranca la aplicación:

```bash
   ./mvnw spring-boot:run
```

5. Cuando veas `Tomcat started on port(s): 8080` y `Started ...Application`, la API está disponible en:

```
   http://localhost:8080/api/v1
```

---

## Endpoints de la API

> Base path: **`/api/v1`**. Todos los endpoints (salvo el login) requieren `Authorization: Bearer <token>`.

### Autenticación

| Método | Ruta | Descripción |
|---|---|---|
| POST | `/login` | Inicia sesión (HTTP Basic) y devuelve un JWT |

### Residentes

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/residents` | Lista todos los residentes |
| GET | `/residents/{id}` | Obtiene un residente |
| POST | `/residents` | Crea un residente |
| PUT | `/residents/{id}` | Actualiza un residente |
| DELETE | `/residents/{id}` | Elimina un residente |

### Habitaciones

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/rooms` | Lista todas las habitaciones |
| GET | `/rooms/{id}` | Obtiene una habitación |
| POST | `/rooms` | Crea una habitación |
| PUT | `/rooms/{id}` | Actualiza una habitación |
| DELETE | `/rooms/{id}` | Elimina una habitación |
| POST | `/rooms/{roomId}/residents/{residentId}` | Asigna un residente a una habitación (409 si está completa) |
| DELETE | `/rooms/{roomId}/residents/{residentId}` | Retira un residente de la habitación |

### Informes

| Método | Ruta | Descripción |
|---|---|---|
| POST | `/reports` | Crea un informe (`{ residentId, content }`) |
| GET | `/residents/{id}/reports` | Lista los informes de un residente |

---

## Autenticación paso a paso

1. **Login** (Basic Auth con las credenciales del admin):

```bash
   curl -X POST http://localhost:8080/api/v1/login \
     -u admin@eldercare.com:admin1234
```

   Respuesta:

```json
   { "token": "<JWT>" }
```

2. **Llamada protegida** con el token:

```bash
   curl http://localhost:8080/api/v1/residents \
     -H "Authorization: Bearer <JWT>"
```

> El token caduca a los **15 minutos**. Si una respuesta trae la cabecera `X-Refresh-Token`, puedes usar ese nuevo token para seguir sin re-login.

---

## Datos de ejemplo (seeding)

Al arrancar, `data.sql` siembra:

- Un **usuario administrador**:
  - email: `admin@eldercare.com`
  - contraseña: `admin1234`
- Un conjunto de **habitaciones** iniciales (individuales y dobles).

La siembra usa `INSERT IGNORE`, de modo que es **idempotente**: se puede ejecutar en cada arranque sin duplicar datos (con `ddl-auto=update`, que conserva la información entre reinicios).

---

## Tests

El proyecto incluye pruebas **unitarias** (servicios, controladores, mappers, enumerados, excepciones, filtros y seguridad) y de **integración de contexto** con **Testcontainers** (levanta un MySQL real en un contenedor efímero).

Ejecutar toda la batería:

```bash
./mvnw clean test
```

> **Requiere Docker en marcha**: el test de contexto usa Testcontainers, que arranca un MySQL en un contenedor. Si Docker está apagado, ese test fallará aunque el resto pase.

Tecnologías de testing: **JUnit 5**, **Mockito**, **AssertJ**, **Testcontainers**.

---

## Decisiones de diseño y alcance

Por tratarse de un PMV con un calendario ajustado, se acotó el alcance de forma consciente:

- **Un único rol (ADMIN)**: no hay registro ni gestión de múltiples usuarios.
- **Sin entidad "Profesional"**: el profesional de referencia es, por ahora, un campo de texto en el residente. El diseño queda preparado para recuperarlo como entidad en el futuro.
- **Habitaciones como infraestructura fija**: se siembran por `data.sql` en lugar de gestionarse desde una UI, al ser datos que rara vez cambian.
- **Capacidad/ocupación calculadas**, no persistidas, para evitar estados inconsistentes.

---

## Estructura del repositorio

```
src/
├── main/
│   ├── java/juanim/dev/eldercare/
│   │   ├── auth/            # TokenService, filtro de refresco, AuthController, DTOs
│   │   ├── security/        # Configuración de seguridad, JpaUserDetailsService
│   │   ├── config/          # Configuración general
│   │   ├── user/            # Entidad User y repositorio
│   │   ├── resident/        # Entidad, DTOs, mapper, servicio, controlador
│   │   ├── room/            # Entidad, DTOs, mapper, servicio, controlador, excepciones
│   │   ├── report/          # Entidad, DTOs, servicio, controlador
│   │   └── globals/         # Excepciones y manejador global
│   └── resources/
│       ├── application.properties
│       ├── application-mysql.properties
│       └── data.sql
└── test/
    └── java/juanim/dev/eldercare/   # Pruebas unitarias y de integración
```

---

## Autor

**<Juan Isidro Menéndez>** — Proyecto final de *Full Stack Developer with Java* (Factoría F5 + Capgemini), Asturias.

Repositorio del frontend: `<https://github.com/JuanIsidroMenendez/eldercare-frontend>`