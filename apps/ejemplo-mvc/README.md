# Ejemplo mínimo: @Controller + Thymeleaf

Versión reducida al mínimo del proyecto `apps/apinasa` para explicar qué es un
**controlador MVC** en Spring Boot: el controlador prepara los datos y Thymeleaf
renderiza el HTML en el servidor.

Mismo stack que ApiNasa: Spring Boot 4.1.1 + Java 25, sin las capas extra
(timeouts, tests con servidor mock, .env...).

## Requisitos

- Java 25
- Conexión a Internet (solo para la página `/epic`, que llama a NASA)

## Ejecutar

Desde la carpeta `ejemplo-mvc`:

```powershell
.\mvnw.cmd spring-boot:run
```

La aplicación arranca en http://localhost:8080 (redirige a `/epic`)

## Rutas

| Ruta | Qué hace |
|---|---|
| `GET /` | Redirige a `/epic`. |
| `GET /epic` | Llama a la API de NASA (EPIC) y muestra los resultados en HTML. |

## API de NASA EPIC

La aplicación consulta el endpoint público `https://epic.gsfc.nasa.gov/api/natural`,
que no requiere clave de API.

## Tests

```powershell
.\mvnw.cmd test
```

Los tests no llaman a NASA: simulan sus respuestas con
`MockRestServiceServer` (la misma técnica que usa apps/apinasa).

## ¿Qué hay que entender aquí?

1. `@Controller` + `@GetMapping` = ruta HTTP que devuelve el nombre de una plantilla.
2. `Model` = los datos que el controlador pasa a la plantilla.
3. Plantillas en `src/main/resources/templates/` (aquí: `epic.html`).
4. `th:text` y `th:each` = las expresiones de Thymeleaf para mostrar datos.
5. `RestClient` = cliente HTTP de Spring para llamar a otras APIs.
6. `RestClient` consulta el endpoint público de EPIC y pasa el JSON a la vista.

## Diferencia con `@RestController`

Un `@RestController` devuelve objetos (JSON); un `@Controller` devuelve HTML
renderizado. Compara con el proyecto hermano `ejemplo-rest`.

## Siguientes pasos (ya vienen en apps/apinasa)

Validación de parámetros (fechas), gestión de errores (4xx/5xx/429),
timeouts, fragmentos de plantilla reutilizables y tests con servidor
HTTP simulado (MockRestServiceServer).
