# MS-MEDICOS

Microservicio para gestión de médicos basado en Spring Boot 3.2.5 con Java 17. Este servicio proporciona una API REST completa para la administración de información médica en un sistema de microservicios.

## Tabla de Contenidos

- [Características](#características)
- [Tecnologías](#tecnologías)
- [Arquitectura](#arquitectura)
- [Configuración](#configuración)
- [Endpoints API](#endpoints-api)
- [Modelo de Datos](#modelo-de-datos)
- [Validaciones](#validaciones)
- [Consola H2](#consola-h2)

## Características

- **Java 17** con Spring Boot 3.2.5
- **Spring Data JPA** para persistencia de datos
- **Base de datos H2** en memoria para desarrollo y pruebas
- **Spring Security** configurado para acceso sin autenticación (desarrollo)
- **Validación** con Jakarta Validation API
- **JWT** (dependencias incluidas para futura implementación)
- **Lombok** para reducir código boilerplate
- **Arquitectura en capas** siguiendo mejores prácticas
- **API REST** con operaciones CRUD completas
- **Gestión de estado activo/inactivo** para médicos
- **Consultas personalizadas** por RUT, email y estado
- **Auditoría automática** con fechas de creación y actualización

## Tecnologías

| Tecnología | Versión | Descripción |
|------------|---------|-------------|
| Java | 17 | Lenguaje de programación |
| Spring Boot | 3.2.5 | Framework de aplicaciones |
| Spring Data JPA | 3.2.5 | Persistencia de datos |
| Spring Security | 3.2.5 | Seguridad |
| H2 Database | - | Base de datos en memoria |
| Lombok | - | Reducción de código boilerplate |
| JWT (JJWT) | 0.12.3 | Tokens de autenticación |
| Maven | - | Gestión de dependencias |

## Arquitectura

El microservicio sigue una arquitectura en capas con separación de responsabilidades:

```
src/main/java/com/microservicios/msmedicos/
├── MsMedicosApplication.java    # Clase principal de Spring Boot
├── controller/                   # Controladores REST
│   └── MedicoController.java    # Endpoints de la API de médicos
├── service/                      # Lógica de negocio
│   └── MedicoService.java       # Servicios de médicos
├── repository/                   # Acceso a datos
│   └── MedicoRepository.java    # Repositorio JPA
├── model/                        # Entidades JPA
│   └── Medico.java              # Entidad Médico
├── dto/                         # Objetos de transferencia de datos
│   └── MedicoDTO.java           # DTO de médico
├── exception/                    # Excepciones personalizadas
│   └── MedicoException.java     # Excepción personalizada
└── security/                    # Configuración de seguridad
    └── SecurityConfig.java      # Configuración de Spring Security
```

### Flujo de Datos

```
Cliente HTTP → Controller → Service → Repository → Base de Datos H2
                ↓           ↓          ↓
            Validación   Lógica    Consultas JPA
            DTO          Negocio    SQL
```

## Configuración

### Configuración por Defecto (application.properties)

```properties
# Servidor
server.port=8083
spring.application.name=ms-medicos

# Base de datos H2
spring.datasource.url=jdbc:h2:mem:medicos_db
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# JPA/Hibernate
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Consola H2
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# Logging
logging.level.com.microservicios=DEBUG
logging.level.org.springframework.security=DEBUG
```
## 🌐 Endpoints API

### Base URL
```
http://localhost:8083/api/medicos
```

### Operaciones CRUD

#### Listar todos los médicos
```http
GET /api/medicos
```
**Response:** `200 OK` - Lista de todos los médicos

#### Listar médicos activos
```http
GET /api/medicos/activos
```
**Response:** `200 OK` - Lista de médicos activos

#### Listar médicos inactivos
```http
GET /api/medicos/inactivos
```
**Response:** `200 OK` - Lista de médicos inactivos

#### Buscar médico por ID
```http
GET /api/medicos/{id}
```
**Response:** `200 OK` - Médico encontrado o `404 Not Found`

#### Buscar médico por RUT
```http
GET /api/medicos/rut/{rut}
```
**Response:** `200 OK` - Médico encontrado o `404 Not Found`

#### Buscar médico por email
```http
GET /api/medicos/email/{email}
```
**Response:** `200 OK` - Médico encontrado o `404 Not Found`

#### Crear nuevo médico
```http
POST /api/medicos
Content-Type: application/json

{
  "rut": "12345678-9",
  "nombre": "Juan",
  "apellido": "Pérez",
  "especialidad": "Cardiología",
  "email": "juan.perez@hospital.cl",
  "telefono": "+56912345678"
}
```
**Response:** `201 Created` - Médico creado

#### Actualizar médico existente
```http
PUT /api/medicos/{id}
Content-Type: application/json

{
  "rut": "12345678-9",
  "nombre": "Juan",
  "apellido": "Pérez",
  "especialidad": "Cardiología Intervencionista",
  "email": "juan.perez@hospital.cl",
  "telefono": "+56987654321",
  "activo": true
}
```
**Response:** `200 OK` - Médico actualizado o `404 Not Found`

#### Eliminar médico
```http
DELETE /api/medicos/{id}
```
**Response:** `204 No Content` - Médico eliminado o `404 Not Found`

#### Activar médico
```http
PUT /api/medicos/{id}/activar
```
**Response:** `200 OK` - Médico activado o `404 Not Found`

#### Desactivar médico
```http
PUT /api/medicos/{id}/desactivar
```
**Response:** `200 OK` - Médico desactivado o `404 Not Found`

## Modelo de Datos

### Entidad Medico

| Campo | Tipo | Restricciones | Descripción |
|-------|------|---------------|-------------|
| id | Long | PK, Auto-generado | Identificador único |
| rut | String | Único, Obligatorio, Max 12 chars | RUT del médico |
| nombre | String | Obligatorio | Nombre del médico |
| apellido | String | Obligatorio | Apellido del médico |
| especialidad | String | Obligatorio | Especialidad médica |
| email | String | Único | Email de contacto |
| telefono | String | Opcional | Teléfono de contacto |
| activo | Boolean | Default: true | Estado del médico |
| fechaCreacion | LocalDateTime | Auto-generado | Fecha de creación |
| fechaActualizacion | LocalDateTime | Auto-generado | Fecha de última actualización |

### DTO MedicoDTO

Objeto de transferencia de datos con la misma estructura que la entidad, utilizado para la comunicación API.

## Validaciones

### Validaciones de Negocio

- **RUT**: 
  - Obligatorio
  - Único en el sistema
  - Longitud máxima de 12 caracteres
  - Se limpia espacios en blanco automáticamente

- **Email**:
  - Único en el sistema (si se proporciona)
  - Se limpia espacios en blanco automáticamente

- **Campos obligatorios**:
  - nombre
  - apellido
  - especialidad
  - rut

- **Campos opcionales**:
  - email
  - telefono

- **Gestión automática**:
  - `activo` se establece en `true` por defecto
  - `fechaCreacion` se genera automáticamente al crear
  - `fechaActualizacion` se actualiza automáticamente al modificar

### Mensajes de Error

- `"El RUT es obligatorio"`
- `"El nombre es obligatorio"`
- `"El apellido es obligatorio"`
- `"La especialidad es obligatoria"`
- `"El RUT ya está registrado: {rut}"`
- `"El email ya está registrado: {email}"`

## Consola H2

### Acceder a la consola H2

```
URL: http://localhost:8083/h2-console
```

## Notas Adicionales

- La base de datos H2 es en memoria, por lo que los datos se pierden al reiniciar la aplicación
- El puerto por defecto es 8083 (configurable en application.properties)
