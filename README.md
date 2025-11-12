# Usuario API — Ejecución rápida

Breve y directo: cómo ejecutar la API por primera vez, localmente y con Docker.

Requisitos

- Docker + Docker Compose (recomendado)
- Git
- Java 17 + Maven si vas a ejecutar localmente sin Docker

# Usuario API — Cómo ejecutar (breve)

Dos formas: Local (tu máquina) o Docker (recomendado). Escoge una.

Requisitos

- Local: Java 17, Maven, PostgreSQL (opcional si usarás Docker)
- Docker: Docker Desktop (incluye Docker Compose), Git

Clonar el repositorio

```bash
git clone https://github.com/isaac1227/spring-boot-usuarios.git
cd spring-boot-usuarios
```

Ejecutar en local

1. Crear la BD y el usuario (si no existe):

```bash
psql -d postgres -c "CREATE ROLE usuario WITH LOGIN PASSWORD 'password';"
psql -d postgres -c "CREATE DATABASE usuario_db OWNER usuario;"
```

2. Compilar y arrancar:

```bash
./mvnw -DskipTests package
./mvnw -DskipTests spring-boot:run
```

3. Acceder: http://localhost:8080

Si tu Postgres local usa otras credenciales, exporta las variables antes de arrancar:

```bash
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/usuario_db
export SPRING_DATASOURCE_USERNAME=usuario
export SPRING_DATASOURCE_PASSWORD=your_password
./mvnw spring-boot:run
```

Ejecutar con Docker (recomendado)

1. Levantar app + Postgres:

```bash
docker compose up --build
```

2. Acceder: http://localhost:8080/swagger-ui/index.html

3. Parar y limpiar:

```bash
docker compose down
docker volume rm spring-boot-usuarios_pgdata  # opcional
```

Notas sobre credenciales

- Docker: `docker-compose.yml` crea Postgres con usuario `usuario` y contraseña `secret`. La app dentro del contenedor usará esas credenciales.
- Local: por defecto la app usa las credenciales de `application.yml` (usuario=`usuario`, password=`password`) a menos que exportes variables.

Tests

- Ejecutar todos los tests:

```bash
./mvnw test
```

- Ejecutar solo unit tests:

```bash
./mvnw -Dtest=**/UserServiceImplUnitTest test
```

Endpoints principales (rápido)

- POST /users — crear usuario
- GET /users/{id} — obtener usuario por id
- GET /users?q={q}&page={page}&size={size} — listar usuarios
- PUT /users/{id} — actualizar usuario
- DELETE /users/{id} — eliminar usuario

Eso es todo: local = necesitas Java/Maven/Postgres; Docker = solo Docker Desktop y `docker compose up --build`.
