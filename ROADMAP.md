# NASA Spring Guide — Roadmap

## 1. Identidad

* [ ] Cambiar nombre del proyecto a `nasa-spring-guide` (README y landing hechos; falta renombrar el repo en GitHub)
* [x] Preparar logo en PNG con fondo transparente
* [x] Aplicar identidad visual del proyecto

  * Spring green `#6DB33F`
  * NASA-inspired deep blue
  * Diseño limpio y minimalista

## 2. Estructura del proyecto

* [x] Pasar el repositorio a estructura monorepo
* [x] Mantener el código actual de `ApiNasa`
* [x] Mantener los ejemplos `ejemplo-mvc` y `ejemplo-rest`
* [x] Separar claramente aplicación, documentación y landing
* [x] No modificar la lógica existente salvo que sea necesario

## 3. Documentación

* [x] Reorganizar `infoapi.md` en una documentación más clara
* [x] Crear una introducción al proyecto
* [x] Documentar las APIs utilizadas

  * [x] Asteroids NeoWs
  * [x] TechTransfer
  * [x] DONKI
  * [x] EPIC
* [x] Documentar los ejemplos existentes
* [x] Explicar los conceptos de Spring Boot utilizados
* [x] Añadir ejemplos de peticiones y respuestas
* [x] Añadir guía de instalación y ejecución
* [x] Mantener la documentación técnica sencilla y orientada al aprendizaje

## 4. README

* [x] Crear un README principal
* [x] Añadir logo
* [x] Presentar brevemente el proyecto
* [x] Explicar qué se aprende
* [x] Mostrar las APIs disponibles
* [x] Añadir tecnologías utilizadas
* [x] Añadir instrucciones de ejecución
* [x] Enlazar con la documentación

## 5. Landing

* [x] Crear una landing sencilla
* [x] Utilizar los colores de la identidad visual
* [x] Mantener un diseño limpio y minimalista
* [x] Presentar el proyecto y sus objetivos
* [x] Mostrar las APIs disponibles
* [x] Enlazar con la documentación y GitHub
* [x] Utilizar `elastic-ui` para los componentes

## 6. Siguientes pasos

* [x] Investigar Postman

  * [x] Probar las APIs de NASA con colecciones sencillas
  * [x] Hacer ejemplos sencillos y documentarlos
  * [ ] Explorar los scripts de Postman y el Visualizer
    * [ ] Escribir una plantilla HTML en la pestaña Scripts y mostrar la respuesta con `pm.visualizer.set`
    * [ ] Hacer un ejemplo sencillo, por ejemplo una tabla con las patentes de TechTransfer
    * [ ] Documentarlo en `docs/postman.md`
* [ ] Investigar GraphQL

  * [ ] Hacer ejemplos sencillos con Spring Boot
  * [ ] Comparar con REST de forma breve
  * [ ] Ver qué pasa cuando la API cambia (campos nuevos, renombrados o eliminados) y qué hacer en el programa para que no se rompa
  * [ ] Probar si GraphQL aguanta mejor esos cambios que REST (pides solo los campos que usas, `@deprecated`) y explicarlo sencillo
* [ ] Hacer presentaciones

  * [ ] Presentación del proyecto
  * [ ] Presentación de Postman y GraphQL
* [x] Añadir `AGENTS.md` con el contexto del proyecto

## 7. Revisión final

* [x] Migrar `apinasa` y la documentación a las URLs actuales de NASA (TechTransfer, DONKI y EPIC cambiaron de servidor)
* [ ] Comprobar que el código original sigue funcionando
* [ ] Revisar enlaces y documentación
* [x] Comprobar que no hay API keys expuestas
* [ ] Revisar README
* [ ] Revisar landing
* [ ] Dejar el repositorio listo para compartir
