# AGENTS.md

Contexto para agentes que trabajen en este repositorio.

## Qué es

NASA Spring Guide: una guía didáctica de Spring Boot sobre APIs públicas de NASA. Es un proyecto de aprendizaje.

## Estructura

- `apps/apinasa`: aplicación completa (Spring Boot + Thymeleaf).
- `apps/ejemplo-mvc`: un `@Controller` mínimo.
- `apps/ejemplo-rest`: un `@RestController` mínimo.
- `docs/`: documentación en Markdown. La landing la lee tal cual.
- `landing/`: web de presentación (Vite + Vue 3 + elastic-ui).
- `ROADMAP.md`: qué está hecho y qué falta.

## Principio principal: mantenerlo simple

- Elige siempre la solución más sencilla que funcione. Si dudas entre dos, la más corta.
- No añadas capas, abstracciones, patrones ni configuración "por si acaso".
- No metas dependencias nuevas si lo que hay ya sirve. Busca primero si algo ya existe.
- Los ejemplos tienen que poder leerse de una vez: pocas clases, nombres claros, sin trucos.
- No refactorices código que funciona salvo que te lo pidan.
- Si algo se vuelve complejo, para y propón una versión más simple antes de seguir.

## Convenciones

- Idioma del repo: español (docs, README, commits). Los commits siguen Conventional Commits y no llevan co-autoría ni atribución de IA.
- Documentación corta, orientada a aprender, con ejemplos de petición y respuesta.
- La API key de NASA va en variable de entorno. No la escribas en el código, en la documentación ni en capturas.

## Comprobaciones

- Java: `mvn test` dentro de cada app.
- Landing: `npm ci && npm run build` dentro de `landing/`.
