INSERT IGNORE INTO inventario (libro_id, cantidad, cantidad_minima, activo, fecha_actualizacion)
VALUES
  (1, 50, 5, true, NOW()),
  (2, 30, 5, true, NOW()),
  (3, 20, 5, true, NOW()),
  (4, 15, 5, true, NOW());
