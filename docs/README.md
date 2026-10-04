# Documentación

NASA Spring Guide es un proyecto para aprender Spring Boot llamando a APIs reales. Hay una aplicación completa y dos ejemplos pequeños que aíslan una idea cada uno. Todo lo que se consulta vive bajo `https://api.nasa.gov`.

## Por dónde empezar

1. [Instalación y ejecución](instalacion.md): requisitos, clave de NASA y comandos.
2. [Ejemplos mínimos](ejemplos.md): `ejemplo-mvc` y `ejemplo-rest`, el camino corto para entender los controladores.
3. [Conceptos de Spring Boot](conceptos-spring.md): los términos que salen en el código.
4. [Las APIs de NASA](apis.md): qué devuelve cada servicio, con peticiones y respuestas de ejemplo.
5. [`apps/apinasa`](../apps/apinasa/README.md): la aplicación completa, con validación, errores y tests.

## Mapa del repositorio

| Carpeta | Contenido |
|---|---|
| `apps/apinasa` | Aplicación MVC con Thymeleaf y las cuatro demos. |
| `apps/ejemplo-mvc` | Un `@Controller` con una plantilla. |
| `apps/ejemplo-rest` | Un `@RestController` que devuelve JSON. |
| `docs/` | Esta documentación. |
| `landing/` | Página de presentación (Vite, Vue 3 y elastic-ui). |
| `assets/` | Logo. |
