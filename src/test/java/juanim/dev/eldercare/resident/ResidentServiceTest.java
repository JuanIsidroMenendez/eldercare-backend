package juanim.dev.eldercare.resident;

public package juanim.dev.eldercare.resident;

import juanim.dev.eldercare.resident.dto.ResidentDTORequest;
import juanim.dev.eldercare.resident.dto.ResidentDTOResponse;
import juanim.dev.eldercare.resident.mapper.ResidentMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)   // activa Mockito en JUnit 5
class ResidentServiceImplTest {

    @Mock
    private ResidentRepository repository;   // dependencia simulada

    @Mock
    private ResidentMapper mapper;           // dependencia simulada

    @InjectMocks
    private ResidentServiceImpl service;     // la clase bajo prueba, con los mocks inyectados

    @Test
    void create_guardaYDevuelveElResidente() {
        // given
        ResidentDTORequest request = new ResidentDTORequest(
                null, "Iker", "Ardu Engo", 85, DependencyGrade.GRADO_I,
                "Calle de la Chavala 45", "Madrid", "28012",
                "Joseppe Ardu", "Hijo", "600123456", "Marta Ruiz");
        ResidentEntity entity = ResidentEntity.builder().firstName("Iker").build();
        ResidentDTOResponse response = new ResidentDTOResponse(
                1L, null, "Iker", "Ardu Engo", 85, DependencyGrade.GRADO_I,
                "Dependencia leve", "Calle de la Chavala 45", "Madrid", "28012",
                "Joseppe Ardu", "Hijo", "600123456", "Marta Ruiz");

        when(mapper.toEntity(request)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toResponse(entity)).thenReturn(response);

        // when
        ResidentDTOResponse result = service.create(request);

        // then
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.firstName()).isEqualTo("Iker");
        verify(repository).save(entity);   // se guardó en el repositorio
    }

    @Test
    void findById_inexistente_lanza404() {
        // given
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // when / then
        assertThatThrownBy(() -> service.findById(99L))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Residente no encontrado");

        verify(mapper, never()).toResponse(any());   // nunca llega a mapear
    }
} {
    
}
