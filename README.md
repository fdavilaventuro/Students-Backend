# Microservicio de Estudiantes

Microservicio RESTful para la gestión de estudiantes desarrollado con Spring Boot y PostgreSQL, contenerizado con Docker.

## 🏗️ Arquitectura

- **Framework**: Spring Boot 3.5.6
- **Base de datos**: PostgreSQL
- **Contenerización**: Docker
- **Lenguaje**: Java 17

## 📋 Características

- CRUD completo de estudiantes
- Perfiles de estudiantes con preferencias en JSONB
- Búsqueda paginada y filtrada por email
- Actualizaciones parciales (PATCH)
- API RESTful completa

## 🚀 Instalación y Despliegue en EC2

### Prerrequisitos
- Instancia EC2 con Amazon Linux 2 o Ubuntu
- Docker instalado
- Java 17 (para compilación local)
- Maven (para compilación local)

### Paso 1: Clonar el repositorio
```bash
git clone https://github.com/fdavilaventuro/Students-Backend.git
cd students-backend
```

### Paso 2: Compilar la aplicación (opcional - si necesitas modificar código)
```bash
mvn clean package
```

### Paso 3: Construir la imagen Docker
```bash
docker build -t microservicio-estudiantes .
```

### Paso 4: Ejecutar la base de datos PostgreSQL
```bash
docker run -d \
  --name postgres-db \
  -e POSTGRES_DB=estudiantesdb \
  -e POSTGRES_USER=estudianteuser \
  -e POSTGRES_PASSWORD=estudiantepass \
  -p 5432:5432 \
  -v postgres_data:/var/lib/postgresql/data \
  postgres:15
```

### Paso 5: Ejecutar la aplicación
```bash
docker run -d \
  --name estudiantes-app \
  --link postgres-db \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://postgres-db:5432/estudiantesdb \
  -e SPRING_DATASOURCE_USERNAME=estudianteuser \
  -e SPRING_DATASOURCE_PASSWORD=estudiantepass \
  -e SPRING_JPA_HIBERNATE_DDL-AUTO=update \
  -p 8080:8080 \
  microservicio-estudiantes
```

### Paso 6: Verificar el despliegue
```bash
# Verificar contenedores activos
docker ps

# Ver logs de la aplicación
docker logs estudiantes-app

# Probar salud del servicio
curl http://<public-ip>:8080/estudiantes
```

## 🔧 Configuración de la EC2

### Security Group
Asegúrate de que el Security Group de tu EC2 permita:
- **Puerto 22** (SSH) - Para conexión
- **Puerto 8080** (HTTP) - Para el microservicio
- **Puerto 5432** (PostgreSQL) - Opcional, para acceso directo a BD

### Comandos útiles para gestión
```bash
# Detener aplicación
docker stop estudiantes-app

# Iniciar aplicación
docker start estudiantes-app

# Ver logs en tiempo real
docker logs -f estudiantes-app

# Eliminar contenedores
docker rm -f estudiantes-app postgres-db

# Eliminar volumen de datos (cuidado: elimina toda la data)
docker volume rm postgres_data
```

## 📚 Endpoints de la API

### GET /estudiantes
Obtiene lista paginada de estudiantes
```bash
curl "http://<public-ip>:8080/estudiantes?page=0&size=10"
```

### GET /estudiantes?email=valor
Búsqueda filtrada por email
```bash
curl "http://<public-ip>:8080/estudiantes?email=ejemplo@correo.com"
```

### GET /estudiantes/{id}
Obtiene un estudiante específico
```bash
curl http://<public-ip>:8080/estudiantes/2f8c9e7e-1234-5678-90ab-cdef12345678
```

### POST /estudiantes
Crea un nuevo estudiante
```bash
curl -X POST http://<public-ip>:8080/estudiantes \
  -H "Content-Type: application/json" \
  -d '{
    "nombres": "Juan",
    "apellidos": "Pérez",
    "email": "juan.perez@example.com",
    "telefono": "+123456789",
    "pais": "España",
    "perfil": {
      "avatarUrl": "https://example.com/avatar.jpg",
      "bio": "Estudiante de negocios digitales",
      "preferencias": "{\"idioma\": \"es\", \"tema\": \"claro\"}"
    }
  }'
```

### PATCH /estudiantes/{id}
Actualización parcial de estudiante
```bash
curl -X PATCH http://<public-ip>:8080/estudiantes/2f8c9e7e-1234-5678-90ab-cdef12345678 \
  -H "Content-Type: application/json" \
  -d '{
    "telefono": "+987654321",
    "pais": "México"
  }'
```

### DELETE /estudiantes/{id}
Elimina un estudiante
```bash
curl -X DELETE http://<public-ip>:8080/estudiantes/2f8c9e7e-1234-5678-90ab-cdef12345678
```

## 🗃️ Estructura de la Base de Datos

### Tabla: estudiante
- `id` UUID (Primary Key)
- `nombres` VARCHAR NOT NULL
- `apellidos` VARCHAR NOT NULL
- `email` VARCHAR UNIQUE NOT NULL
- `telefono` VARCHAR
- `pais` VARCHAR
- `fecha_creacion` TIMESTAMP
- `fecha_actualizacion` TIMESTAMP

### Tabla: estudiante_perfil
- `id` UUID (Primary Key, Foreign Key a estudiante)
- `avatar_url` VARCHAR
- `bio` TEXT
- `preferencias` JSONB

## 🔄 Flujo de Comunicación

```
Microservicio Agregador → (HTTP/REST) → Microservicio Estudiantes (Puerto 8080)
                                      ↓
                                  PostgreSQL (Puerto 5432)
```

## 🐛 Troubleshooting

### Error de conexión a base de datos
```bash
# Verificar que PostgreSQL esté corriendo
docker ps | grep postgres

# Ver logs de PostgreSQL
docker logs postgres-db
```

### Error de puerto en uso
```bash
# Verificar procesos en puerto 8080
netstat -tulpn | grep 8080

# Si hay conflicto, cambiar puerto en el comando run:
-p 8081:8080
```

### La aplicación no inicia
```bash
# Ver logs detallados
docker logs estudiantes-app

# Verificar variables de entorno
docker inspect estudiantes-app
```

## 📞 Soporte

Para issues o preguntas, escribir a [@fdavilaventuro](https://www.github.com/fdavilaventuro)
