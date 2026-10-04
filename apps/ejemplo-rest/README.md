# Ejemplo mínimo: @RestController

Versión reducida al mínimo del proyecto `apps/apinasa` para explicar qué es un
**controlador REST** en Spring Boot: devuelve **datos (JSON)**, no páginas HTML.

Mismo stack que ApiNasa: Spring Boot 4.1.1 + Java 25.

## Requisitos

- Java 25
- Conexión a Internet (solo para el endpoint `/api/epic`, que llama a NASA)

## Ejecutar

Desde la carpeta `ejemplo-rest`:

```powershell
.\mvnw.cmd spring-boot:run
```

La aplicación arranca en http://localhost:8080

## Rutas

| Ruta | Qué hace |
|---|---|
| `GET /api/saludo?nombre=Ada` | Endpoint local: devuelve JSON sin llamar a nadie. Ideal para probar. |
| `GET /api/epic` | Llama a la API de NASA (EPIC) con `RestClient` y devuelve la lista de imágenes como JSON. |

## La clave de NASA

Por defecto se usa `DEMO_KEY` (cuota muy limitada: 30 peticiones/hora por IP).
Para usar tu clave personal (gratis en https://api.nasa.gov/), defínela como
variable de entorno en la misma sesión de PowerShell:

```powershell
$env:NASA_API_KEY = "pega_aqui_tu_clave"
.\mvnw.cmd spring-boot:run
```

## Tests

```powershell
.\mvnw.cmd test
```

Los tests no llaman a NASA: prueban el endpoint local directamente y
simulan las respuestas de NASA con `MockRestServiceServer` (la misma
técnica que usa apps/apinasa).

## ¿Qué hay que entender aquí?

1. `@RestController` + `@GetMapping` = ruta HTTP que devuelve datos.
2. `@RequestParam` = query parameter (?nombre=Ada).
3. `RestClient` = cliente HTTP de Spring para llamar a otras APIs.
4. `UriComponentsBuilder` = construye la URL codificando parámetros.
5. Los `record` = DTOs que representan el JSON de la API; Jackson los convierte solo.
6. `ParameterizedTypeReference` = necesario para deserializar **listas** con genéricos.

## Diferencia con `@Controller`

Un `@Controller` devuelve el nombre de una plantilla Thymeleaf (HTML); un
`@RestController` devuelve objetos que Spring convierte a JSON. Compara con el
proyecto hermano `ejemplo-mvc`.

## Siguientes pasos (ya vienen en apps/apinasa)

Gestión de errores (4xx/5xx/429), timeouts, validación de parámetros,
redacción de la clave en las respuestas y tests con servidor HTTP
simulado (MockRestServiceServer).
