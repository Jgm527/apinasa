# Documentación

NASA Spring Guide es un proyecto para aprender Spring Boot llamando a APIs reales. Hay una aplicación completa y dos ejemplos pequeños que aíslan una idea cada uno. Las APIs que se consultan son públicas y viven en distintos servidores de NASA; solo una pide clave.

## Por dónde empezar

1. [Tu primera petición](primera-peticion.md): una petición real de principio a fin, con glosario.
2. [Probar con Postman](postman.md): explora las APIs con peticiones HTTP desde una interfaz gráfica.
3. [Instalación y ejecución](instalacion.md): requisitos, clave de NASA y comandos.
4. [Ejemplos mínimos](ejemplos.md): `ejemplo-mvc` y `ejemplo-rest`, el camino corto para entender los controladores.
5. [Conceptos de Spring Boot](conceptos-spring.md): los términos que salen en el código.
6. [Las APIs de NASA](apis.md): qué devuelve cada servicio, con peticiones y respuestas de ejemplo.
7. [`apps/apinasa`](../apps/apinasa/README.md): la aplicación completa, con validación, errores y tests.

## Mapa del repositorio

| Carpeta | Contenido |
|---|---|
| `apps/apinasa` | Aplicación MVC con Thymeleaf y las cuatro demos. |
| `apps/ejemplo-mvc` | Un `@Controller` con una plantilla. |
| `apps/ejemplo-rest` | Un `@RestController` que devuelve JSON. |
| `docs/` | Esta documentación. |
| `landing/` | Página de presentación (Vite, Vue 3 y elastic-ui). |
| `assets/` | Logo. |
