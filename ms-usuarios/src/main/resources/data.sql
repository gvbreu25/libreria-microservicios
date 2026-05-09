-- Contraseña de ambos usuarios: 123456
INSERT IGNORE INTO usuarios (nombre, apellido, email, password, rol, activo, fecha_creacion)
VALUES
  ('Admin', 'Sistema', 'admin@libreria.com',
   '$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0/W7CiqD2',
   'ADMIN', true, NOW()),
  ('Juan', 'Perez', 'juan.perez@email.com',
   '$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0/W7CiqD2',
   'CLIENTE', true, NOW());
