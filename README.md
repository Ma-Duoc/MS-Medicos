# MS-MEDICOS

Microservicio para gestión de médicos basado en Spring Boot 3.2.5 con Java 17.

## Características

- **Java 17** con Spring Boot 3.2.5
- **Spring Data JPA** para persistencia
- **Base de datos H2** en memoria
- **Spring Security** con autenticación básica
- **Validación** con Jakarta Validation
- **JWT** para tokens de autenticación
- **Lombok** para reducir código boilerplate

## Arquitectura

El microservicio sigue una arquitectura en capas:

```
src/main/java/com/microservicios/msmedicos/
├── controller/     # Controladores REST
├── service/        # Lógica de negocio
├── repository/     # Acceso a datos
├── model/          # Entidades JPA
├── dto/           # Objetos de transferencia
├── exception/     # Excepciones personalizadas
└── MsMedicosApplication.java
```

## Entidad Médico

La entidad `Medico` contiene los siguientes campos:

- **id**: Long (autogenerado, primary key)
- **rut**: String (único y obligatorio)
- **nombre**: String (obligatorio)
- **apellido**: String (obligatorio)
- **especialidad**: String (obligatorio)
- **email**: String (único)
- **telefono**: String
- **activo**: Boolean (por defecto true)
- **fechaCreacion**: LocalDateTime (autogenerado)
- **fechaActualizacion**: LocalDateTime (autogenerado)

## Endpoints API

### Médicos

- `GET /api/medicos` - Listar todos los médicos
- `GET /api/medicos/activos` - Listar médicos activos
- `GET /api/medicos/inactivos` - Listar médicos inactivos
- `GET /api/medicos/{id}` - Buscar médico por ID
- `GET /api/medicos/rut/{rut}` - Buscar médico por RUT
- `GET /api/medicos/email/{email}` - Buscar médico por email
- `POST /api/medicos` - Crear nuevo médico
- `PUT /api/medicos/{id}` - Actualizar médico existente
- `DELETE /api/medicos/{id}` - Eliminar médico
- `PUT /api/medicos/{id}/activar` - Activar médico
- `PUT /api/medicos/{id}/desactivar` - Desactivar médico

## Configuración

- **Puerto**: 8082
- **Base de datos H2**: http://localhost:8082/h2-console
- **Usuario/Password**: admin/admin

## Ejecución

```bash
mvn spring-boot:run
```

## Validaciones

- RUT debe ser único
- Email debe ser único y válido
- Nombre, apellido y especialidad son obligatorios
- El campo `activo` se establece en `true` por defecto
- Las fechas de creación y actualización se gestionan automáticamente
