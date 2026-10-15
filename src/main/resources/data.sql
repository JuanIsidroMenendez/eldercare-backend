-- Usuario administrador inicial (para poder entrar la primera vez). admin1234.
-- Ignore: si data.sql se ejecuta en cada arranque evita el error de email duplicado.

INSERT IGNORE INTO users (email, password, role) 
VALUES ('admin@eldercare.com', '$2b$10$h8ir6M9Cv0ST0hVKGn0Jie7rPUn1fED8EzyeXYtBIY26B9n6tIpOu', 'ADMIN');

-- Habitaciones. Se siembran por ser estructura fija (no es algo que cambie).

INSERT IGNORE INTO rooms (number, type) VALUES
  ('101A', 'INDIVIDUAL'),
  ('102A', 'INDIVIDUAL'),
  ('103A', 'INDIVIDUAL'),
  ('201B', 'DOUBLE'),
  ('202B', 'DOUBLE'),
  ('203B', 'DOUBLE'),
  ('301C', 'DOUBLE'),
  ('302A', 'DOUBLE');