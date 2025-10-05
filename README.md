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
- Puedes usar el archivo cloudformation.yml como plantilla para crear la MV

### Paso 1: Clonar el repositorio
```bash
git clone https://github.com/fdavilaventuro/Students-Backend.git
cd Students-Backend
```

### Paso 2: Instalar maven y compilar la aplicación (ignora tests)
```bash
docker run -it --rm -v "$(pwd)":/usr/src/mymaven -w /usr/src/mymaven maven:3 mvn clean package -DskipTests
```

### Paso 3: Construir la imagen Docker
```bash
docker build -t microservicio-estudiantes .
```

### Paso 4: Ejecutar el docker compose
```bash
docker-compose up --build -d
```

### Paso 5: Verificar el despliegue
```bash
# Verificar contenedores activos
docker compose ps

# Ver logs de la aplicación
docker compose logs app

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
docker compose stop app

# Iniciar aplicación
docker compose start app

# Ver logs en tiempo real
docker compose logs -f app

# Eliminar contenedores
docker compose rm -sf app db

# Eliminar volumen de datos (cuidado: elimina toda la data)
docker compose down -v
```

## 📚 Endpoints de la API

### Vista interactiva en swagger-ui
```bash
http://<public-ip>:8080/swagger-ui.html
```

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
    "nombres": "Ana",
    "apellidos": "Gómez",
    "email": "ana.gomez@example.com",
    "telefono": "+123456789",
    "pais": "España"
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
