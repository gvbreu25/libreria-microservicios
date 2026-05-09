# Librería Online - Sistema de Microservicios

## Descripción
Sistema de gestión de librería online desarrollado con arquitectura de microservicios usando Spring Boot 3.5.14 y Java 21. Implementa el patrón CSR (Controller-Service-Repository), persistencia real con JPA/Hibernate, validaciones con Bean Validation, manejo centralizado de excepciones y comunicación inter-servicios con WebClient.

## Integrante
- Gabriel Abreu

## Tecnologías
- Java 21
- Spring Boot 3.5.14
- Spring Cloud Netflix Eureka (Service Discovery)
- Spring Cloud Gateway
- Spring Security + BCrypt
- Spring Data JPA + Hibernate
- Spring Validation (Bean Validation JSR 380)
- Spring WebFlux (WebClient)
- MySQL 8
- Lombok
- SLF4J + Logback (logs JSON con Logstash)

## Arquitectura

| Microservicio | Puerto | Descripción |
|---|---|---|
| ms-eureka | 8761 | Servidor de descubrimiento (Service Registry) |
| ms-gateway | 8090 | API Gateway |
| ms-usuarios | 8081 | Gestión de usuarios + autenticación BCrypt |
| ms-libros | 8082 | Gestión de libros |
| ms-categorias | 8083 | Gestión de categorías |
| ms-inventario | 8084 | Control de stock |
| ms-carrito | 8085 | Carrito de compras |
| ms-pedidos | 8086 | Gestión de pedidos + detalle (relación JPA OneToMany) |
| ms-pagos | 8087 | Procesamiento de pagos |
| ms-resenas | 8088 | Reseñas y calificaciones |

## Características técnicas

### Persistencia (JPA + Hibernate)
- Entidades con `@Entity`, `@Id`, `@GeneratedValue`, `@Column`, `@Enumerated`
- Repositorios `JpaRepository` con métodos derivados (findBy*, existsBy*)
- DDL automático con `spring.jpa.hibernate.ddl-auto=update`
- Cada microservicio posee su propia base de datos (database-per-service)

### Relación JPA (ms-pedidos)
- `Pedido` ↔ `DetallePedido` con `@OneToMany` / `@ManyToOne`
- Cascada `CascadeType.ALL` y `orphanRemoval=true`
- Foreign Key explícita con `@JoinColumn` y `@ForeignKey`
- Serialización JSON segura con `@JsonManagedReference` / `@JsonBackReference`

### Validaciones (Bean Validation JSR 380)
- DTOs separados de entidades para validar entradas
- Anotaciones `@NotNull`, `@NotBlank`, `@Positive`, `@Email`, `@Min`, `@Size`
- Validación automática con `@Valid` en controladores
- Mensajes personalizados por campo

### Manejo de excepciones
- Excepciones tipadas: `RecursoNoEncontradoException` (404), `ReglaNegocioException` (409)
- `GlobalExceptionHandler` con `@RestControllerAdvice` en cada microservicio
- Códigos HTTP correctos:
  - `404 Not Found` → recurso inexistente
  - `409 Conflict` → violación de regla de negocio (ej: ISBN duplicado)
  - `400 Bad Request` → validación fallida o argumento inválido
  - `500 Internal Server Error` → error inesperado
- Respuesta estructurada con timestamp, status, error y mensaje

### Logs estructurados
- SLF4J en todas las capas (Controller, Service, ClientService, Handler)
- Trazas de eventos clave: creación, actualización, errores, validaciones fallidas
- Output dual: archivo `.log` por microservicio + JSON para Logstash

### Comunicación entre microservicios (WebClient)
- `ms-pedidos → ms-inventario` (verifica stock antes de crear pedido)
- `ms-carrito → ms-libros` (obtiene precio del libro)
- `ms-pagos → ms-pedidos` (verifica que el pedido existe)
- WebClient con `@LoadBalanced` (descubrimiento vía Eureka)
- Timeouts configurados:
  - Connect timeout: 5s
  - Response timeout: 5s
  - Read/Write timeout: 5s
- Manejo de errores y respuestas vacías con `onErrorResume`

### Patrón CSR
```
controller/  → recibe HTTP, delega al service, devuelve ResponseEntity
service/     → lógica de negocio, transacciones, validaciones de dominio
repository/  → acceso a datos vía JpaRepository
model/       → entidades JPA
dto/         → objetos de transferencia con validaciones
exception/   → excepciones tipadas + GlobalExceptionHandler
config/      → SecurityConfig, WebClientConfig
```

## Requisitos previos
- Java 21
- Maven 3.9+
- MySQL 8 (XAMPP recomendado)
- IntelliJ IDEA / VS Code

## Cómo ejecutar

1. Iniciar XAMPP y MySQL
2. Crear las bases de datos en phpMyAdmin (ver lista abajo)
3. Ejecutar los microservicios en este orden:
   1. `ms-eureka` (debe estar arriba antes que el resto)
   2. `ms-gateway`
   3. `ms-usuarios`
   4. `ms-libros`
   5. `ms-categorias`
   6. `ms-inventario`
   7. `ms-carrito`
   8. `ms-pedidos`
   9. `ms-pagos`
   10. `ms-resenas`

## Bases de datos requeridas
- `db_usuarios`
- `db_libros`
- `db_categorias`
- `db_inventario`
- `db_carrito`
- `db_pedidos`
- `db_pagos`
- `db_resenas`

## Endpoints principales

### ms-usuarios (8081)
- `GET    /api/v1/usuarios` - Listar
- `GET    /api/v1/usuarios/{id}` - Buscar
- `POST   /api/v1/usuarios` - Crear
- `PUT    /api/v1/usuarios/{id}` - Actualizar
- `DELETE /api/v1/usuarios/{id}` - Eliminar

### ms-libros (8082)
- `GET    /api/v1/libros` - Listar
- `GET    /api/v1/libros/activos` - Listar activos
- `GET    /api/v1/libros/{id}` - Buscar
- `POST   /api/v1/libros` - Crear
- `PUT    /api/v1/libros/{id}` - Actualizar
- `DELETE /api/v1/libros/{id}` - Eliminar

### ms-categorias (8083)
- `GET    /api/v1/categorias` - Listar
- `GET    /api/v1/categorias/activas` - Listar activas
- `GET    /api/v1/categorias/{id}` - Buscar
- `POST   /api/v1/categorias` - Crear
- `PUT    /api/v1/categorias/{id}` - Actualizar
- `DELETE /api/v1/categorias/{id}` - Eliminar

### ms-inventario (8084)
- `GET    /api/v1/inventario` - Listar
- `GET    /api/v1/inventario/bajo-stock` - Listar libros con bajo stock
- `GET    /api/v1/inventario/libro/{libroId}` - Stock por libro
- `POST   /api/v1/inventario` - Crear registro
- `PUT    /api/v1/inventario/{id}` - Actualizar
- `DELETE /api/v1/inventario/{id}` - Eliminar

### ms-carrito (8085)
- `GET    /api/v1/carrito` - Listar
- `GET    /api/v1/carrito/usuario/{usuarioId}` - Carrito por usuario
- `POST   /api/v1/carrito` - Agregar item
- `PUT    /api/v1/carrito/{id}` - Actualizar item
- `DELETE /api/v1/carrito/{id}` - Quitar item
- `DELETE /api/v1/carrito/usuario/{usuarioId}/vaciar` - Vaciar carrito

### ms-pedidos (8086)
- `GET    /api/v1/pedidos` - Listar
- `GET    /api/v1/pedidos/usuario/{usuarioId}` - Pedidos por usuario
- `GET    /api/v1/pedidos/estado/{estado}` - Pedidos por estado
- `GET    /api/v1/pedidos/{id}` - Buscar (incluye detalles)
- `POST   /api/v1/pedidos` - Crear pedido con detalles + verifica stock
- `PATCH  /api/v1/pedidos/{id}/estado?estado=...` - Cambiar estado
- `DELETE /api/v1/pedidos/{id}` - Eliminar

#### Body de ejemplo POST /api/v1/pedidos
```json
{
  "usuarioId": 1,
  "total": 35990,
  "direccionEnvio": "Av. Principal 123",
  "detalles": [
    { "libroId": 1, "cantidad": 2, "precioUnitario": 12990 },
    { "libroId": 3, "cantidad": 1, "precioUnitario": 9990 }
  ]
}
```

### ms-pagos (8087)
- `GET    /api/v1/pagos` - Listar
- `GET    /api/v1/pagos/pedido/{pedidoId}` - Pagos por pedido
- `POST   /api/v1/pagos` - Procesar pago (verifica que pedido existe)
- `PATCH  /api/v1/pagos/{id}/estado?estado=...` - Cambiar estado
- `DELETE /api/v1/pagos/{id}` - Eliminar

### ms-resenas (8088)
- `GET    /api/v1/resenas` - Listar
- `GET    /api/v1/resenas/libro/{libroId}` - Reseñas por libro
- `GET    /api/v1/resenas/usuario/{usuarioId}` - Reseñas por usuario
- `POST   /api/v1/resenas` - Crear (calificación 1-5)
- `PUT    /api/v1/resenas/{id}` - Actualizar
- `DELETE /api/v1/resenas/{id}` - Eliminar
