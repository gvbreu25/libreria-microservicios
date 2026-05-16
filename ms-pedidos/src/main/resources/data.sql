-- Tabla pedidos y detalle_pedido se crean automáticamente con JPA.
INSERT IGNORE INTO pedidos (id, usuario_id, total, estado, direccion_envio, activo, fecha_pedido)
VALUES
  (1, 2, 29990, 'PENDIENTE', 'Av. Providencia 123, Santiago', true, NOW());

INSERT IGNORE INTO detalle_pedido (id, pedido_id, libro_id, cantidad, precio_unitario, subtotal)
VALUES
  (1, 1, 1, 1, 14990, 14990),
  (2, 1, 2, 1, 15000, 15000);
