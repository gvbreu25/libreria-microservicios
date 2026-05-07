# Librería Online - Sistema de Microservicios

## Descripción
Sistema de gestión de librería online desarrollado con arquitectura de microservicios usando Spring Boot 3.5.14 y Java 21.

## Integrantes
- Gabriel Abreu.

## Tecnologías
- Java 21
- Spring Boot 3.5.14
- Spring Cloud Netflix Eureka
- Spring Security + BCrypt
- Spring Data JPA
- MySQL
- Lombok
- WebClient

## Microservicios

| Microservicio | Puerto | Descripción |
|---|---|---|
| ms-eureka | 8761 | Servidor de descubrimiento |
| ms-gateway | 8090 | API Gateway |
| ms-usuarios | 8081 | Gestión de usuarios |
| ms-libros | 8082	 | Gestión de libros |
| ms-categorias | 8083 | Gestión de categorías |
| ms-inventario | 8084 | Control de inventario |
| ms-carrito | 8085 | Carrito de compras |
| ms-pedidos | 8086 | Gestión de pedidos |
| ms-pagos | 8087 | Procesamiento de pagos |
| ms-resenas | 8088 | Reseñas de libros |

## Requisitos previos
- Java 21
- Maven
- MySQL (XAMPP)
- IntelliJ IDEA

## Cómo ejecutar

1. Iniciar XAMPP y MySQL
2. Crear las bases de datos en phpMyAdmin
3. Ejecutar en este orden:
   - ms-eureka
   - ms-gateway
   - ms-usuarios
   - ms-libros
   - ms-categorias
   - ms-inventario
   - ms-carrito
   - ms-pedidos
   - ms-pagos
   - ms-resenas

## Bases de datos requeridas
- db_usuarios
- db_libros
- db_categorias
- db_inventario
- db_carrito
- db_pedidos
- db_pagos
- db_resenas

## Comunicación entre microservicios
- ms-pedidos → ms-inventario (verifica stock disponible)
- ms-carrito → ms-libros (obtiene precio del libro)
- ms-pagos → ms-pedidos (verifica que el pedido existe)

## Endpoints principales

### ms-usuarios (8081)
- POST /api/v1/usuarios - Crear usuario
- GET /api/v1/usuarios - Listar usuarios
- GET /api/v1/usuarios/{id} - Buscar usuario
- PUT /api/v1/usuarios/{id} - Actualizar usuario
- DELETE /api/v1/usuarios/{id} - Eliminar usuario

### ms-libros (8082)
- POST /api/v1/libros - Crear libro
- GET /api/v1/libros - Listar libros
- GET /api/v1/libros/{id} - Buscar libro
- PUT /api/v1/libros/{id} - Actualizar libro
- DELETE /api/v1/libros/{id} - Eliminar libro

### ms-pedidos (8086)
- POST /api/v1/pedidos - Crear pedido
- GET /api/v1/pedidos - Listar pedidos
- PATCH /api/v1/pedidos/{id}/estado - Actualizar estado

### ms-pagos (8087)
- POST /api/v1/pagos - Procesar pago
- GET /api/v1/pagos - Listar pagos