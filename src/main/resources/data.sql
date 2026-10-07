-- Usuario administrador inicial (para poder entrar la primera vez). admin1234.
-- Ignore: si data.sql se ejecuta en cada arranque evita el error de email duplicado.

INSERT IGNORE INTO users (email, password, role)
VALUES ('admin@eldercare.com', '$2b$10$h8ir6M9Cv0ST0hVKGn0Jie7rPUn1fED8EzyeXYtBIY26B9n6tIpOu', 'ADMIN');