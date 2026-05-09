INSERT IGNORE INTO libros (titulo, autor, isbn, precio, descripcion, categoria_id, activo, fecha_creacion)
VALUES
  ('Clean Code', 'Robert C. Martin', '978-0132350884', 29990, 'Guía de buenas prácticas de programación', 4, true, NOW()),
  ('El Señor de los Anillos', 'J.R.R. Tolkien', '978-0618640157', 24990, 'Épica fantasía de Tolkien', 1, true, NOW()),
  ('Sapiens', 'Yuval Noah Harari', '978-0062316097', 19990, 'Historia de la humanidad', 3, true, NOW()),
  ('Spring Boot in Action', 'Craig Walls', '978-1617292545', 34990, 'Desarrollo con Spring Boot', 4, true, NOW());
