Uso de Docker y Docker Compose

Este proyecto incluye un `Dockerfile` y un `docker-compose.yml` para facilitar el arranque local sin instalar Postgres ni tocar el sistema.

Requisitos

- Docker y Docker Compose (Docker Desktop o CLI).

Levantar la aplicación y la BD

Desde la raíz del proyecto:

```bash
# construir y levantar (fácil)
docker compose up --build
```

- El servicio `app` se construye desde el `Dockerfile`. Expone el puerto 8080.
- El servicio `postgres` usa la imagen oficial y expone el puerto 5432.
- Variables por defecto:
  - DB: `usuario_db`
  - USER: `usuario`
  - PASS: `secret`

Parar y borrar contenedores

```bash
docker compose down
```

Borrar el volumen de datos (resetear la BD)

```bash
docker volume rm spring-boot-usuarios_pgdata
```

Perfil `dev` y `application-dev.yml`

- El `docker-compose.yml` activa `SPRING_PROFILES_ACTIVE=dev`. El proyecto incluye `src/main/resources/application-dev.yml` que lee la conexión a BD de las variables de entorno.
- Si quieres cambiar credenciales o el nombre de la BD, edita `docker-compose.yml` o exporta variables antes de ejecutar.

Consejos

- Para CI, usa Testcontainers en los tests para levantar BD efímeras en cada job.
- Si no quieres construir la app en el contenedor cada vez, puedes compilar con `./mvnw -DskipTests package` y luego usar una imagen runtime que copie el JAR resultante.
