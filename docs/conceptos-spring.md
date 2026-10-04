# Conceptos de Spring Boot

Los que aparecen en el código del repositorio.

| Concepto | Para qué sirve | Dónde verlo |
|---|---|---|
| `@Controller` | Recibe peticiones y devuelve el nombre de una plantilla. | `ejemplo-mvc`, `apps/apinasa/.../web` |
| `@RestController` | Igual, pero devuelve datos que se serializan a JSON. | `ejemplo-rest` |
| `@GetMapping` | Asocia un método con una ruta `GET`. | Todos los controladores |
| `@RequestParam` | Lee un query parameter (`?nombre=Ada`). | `ejemplo-rest` |
| `Model` | Datos que el controlador pasa a la plantilla. | `ejemplo-mvc` |
| Thymeleaf | Motor de plantillas HTML; `th:text` y `th:each` pintan datos. | `templates/` |
| `@Value` | Inyecta una propiedad de `application.properties`. | `nasa.api-key` |
| Inyección por constructor | Spring pasa las dependencias al crear el objeto. | Todos los controladores |
| `RestClient` | Cliente HTTP para llamar a otras APIs. | `NasaHttpSupport`, `ejemplo-rest` |
| `UriComponentsBuilder` | Construye URLs codificando los parámetros. | Servicios de `apps/apinasa` |
| `record` | Clase inmutable corta; sirve de DTO para el JSON. | `NasaModels` |
| Jackson | Convierte JSON a objetos y al revés. | Automático |
| `MockRestServiceServer` | Simula respuestas HTTP en los tests. | `src/test` |

## Flujo de una petición MVC

1. El navegador hace `GET /asteroids?...`.
2. El controlador valida fechas y rangos antes de gastar cuota.
3. El servicio construye la URL a `api.nasa.gov` y llama con `RestClient`.
4. `NasaHttpSupport` guarda el cuerpo bruto, el estado y el `Content-Type`, y convierte el JSON a un DTO.
5. El controlador añade el resultado al `Model` y devuelve la plantilla.
6. Thymeleaf genera el HTML.

## Configuración

`application.properties` define el puerto (`server.port=${PORT:8080}`) y la clave (`nasa.api-key=${NASA_API_KEY:DEMO_KEY}`). La sintaxis `${VARIABLE:valor}` usa la variable de entorno si existe y, si no, el valor por defecto.
