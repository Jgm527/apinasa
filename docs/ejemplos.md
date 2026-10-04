# Ejemplos mínimos

Son versiones reducidas de `apps/apinasa`. Sin validación de fechas, sin gestión de errores y sin fragmentos: cada uno enseña una sola cosa.

## ejemplo-mvc

Un `@Controller` con Thymeleaf. El controlador llama a EPIC, mete el resultado en el `Model` y devuelve el nombre de una plantilla. El servidor genera el HTML.

| Ruta | Qué hace |
|---|---|
| `GET /` | Redirige a `/epic`. |
| `GET /epic` | Llama a EPIC y pinta la lista de imágenes. |

Archivos que mirar: `web/NasaController.java` y `templates/epic.html`.

## ejemplo-rest

Un `@RestController`. Los métodos devuelven objetos y Spring los convierte a JSON con Jackson.

| Ruta | Qué hace |
|---|---|
| `GET /api/saludo?nombre=Ada` | Responde `{"mensaje":"¡Hola, Ada!"}` sin llamar a nadie. |
| `GET /api/epic` | Llama a EPIC con `RestClient` y devuelve la lista como JSON. |

Archivo que mirar: `web/NasaRestController.java`, que está comentado paso a paso.

## Cuándo usar cada uno

Un `@Controller` devuelve una vista: el navegador recibe HTML. Un `@RestController` devuelve datos: el cliente (otra app, JavaScript) recibe JSON. Los dos pueden convivir en el mismo proyecto.

## Siguiente paso

`apps/apinasa` añade lo que aquí falta: validación de parámetros, control de `4xx`, `5xx` y `429`, timeouts, redacción de la clave y tests con `MockRestServiceServer`.
