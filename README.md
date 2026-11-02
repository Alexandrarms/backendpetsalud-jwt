# Backend Clínica Petsalud

Backend académico basado en Spring Boot 3.3.6, Java 17, Spring Data JPA, MySQL y Spring Security. Se conserva la estructura funcional original y se incorpora autenticación JWT.

## Seguridad incorporada
- Inicio de sesión en `POST /api/auth/login` con correo y contraseña.
- Contraseñas nuevas almacenadas con BCrypt.
- Token JWT firmado con expiración configurable.
- Filtro `OncePerRequestFilter` que valida tokens Bearer en cada solicitud protegida.
- Sesiones sin estado (`STATELESS`) y autorización por roles `CLIENTE`, `VETERINARIO` y `ADMIN`.
- El registro público asigna siempre el rol `CLIENTE`, evitando la autoasignación de permisos privilegiados.
- La contraseña se excluye de las respuestas JSON.

## Permisos
| Rutas | Acceso |
|---|---|
| `POST /api/auth/login`, `POST /api/usuarios/registro` | Público |
| `/api/usuarios/**`, `/api/ventas/**` | ADMIN |
| `/api/productos/**`, `/api/expedientes/**` | VETERINARIO, ADMIN |
| `/api/citas/**`, `/api/mascotas/**` | CLIENTE, VETERINARIO, ADMIN |
| `/api/notificaciones/**`, `/api/recompensas/**` | Usuario autenticado |
| Otras rutas | Denegadas |

## Configuración local
La configuración incluida apunta al contenedor Docker académico `petsalud-mysql`: puerto local `3308`, base `petsalud_db`, usuario `root`. La contraseña se puede cambiar mediante `DB_PASSWORD`. El contenedor debe estar iniciado antes de ejecutar la aplicación.

La clave JWT incluida es solo para desarrollo académico. En otro entorno configurar `JWT_SECRET` con una clave Base64 de al menos 32 bytes y `JWT_EXPIRATION_MS` con la duración en milisegundos. No usar la clave de desarrollo en producción.

## Ejecutar
```bash
./mvnw spring-boot:run
```

## Registrar usuario
`POST http://localhost:8080/api/usuarios/registro`
```json
{
  "email": "cliente@petsalud.cl",
  "password": "ClaveDemo123!",
  "nombre": "Cliente",
  "apellido": "Petsalud",
  "telefono": "912345678",
  "direccion": "Concepción"
}
```
El rol se asigna automáticamente como `CLIENTE`.

## Iniciar sesión
`POST http://localhost:8080/api/auth/login`
```json
{ "email": "cliente@petsalud.cl", "password": "ClaveDemo123!" }
```
La respuesta contiene `token`, `tipo`, `email` y `rol`. Para acceder a una ruta protegida enviar `Authorization: Bearer <token>`.

## Consideraciones
CSRF está deshabilitado porque la API utiliza tokens Bearer y no sesiones ni cookies. En producción se debe utilizar HTTPS, una clave secreta robusta administrada fuera del repositorio, validación de entradas y autorización a nivel de propietario del recurso. La autenticación JWT no sustituye la verificación de que cada usuario solo consulte o modifique sus propios registros.
