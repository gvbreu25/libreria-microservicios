INSERT IGNORE INTO categorias (nombre, descripcion, activo, fecha_creacion)
VALUES
  ('Ficción', 'Novelas y cuentos de ficción', true, NOW()),
  ('Ciencia', 'Libros de ciencia y tecnología', true, NOW()),
  ('Historia', 'Libros de historia y geografía', true, NOW()),
  ('Programación', 'Libros de desarrollo de software', true, NOW()),
  ('Infantil', 'Literatura infantil y juvenil', true, NOW());
