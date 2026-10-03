# NASA Lab

Aplicación educativa MVC con Spring Boot, Thymeleaf y APIs públicas de NASA. Permite observar el recorrido de una petición web: formulario, controlador, servicio HTTP, DTO y plantilla.

## Ejecutar

Requisitos: Java 25 (el proyecto declara `java.version=25`) y conexión a Internet para consultar servicios externos.

Solicita tu clave personal gratuita en [api.nasa.gov](https://api.nasa.gov/). En PowerShell, desde `ApiNasa/apinasa`, crea tu archivo local y ábrelo para pegar la clave:

```powershell
Copy-Item .env.example .env
notepad .env
```

En `.env`, cambia el valor de ejemplo:

```dotenv
NASA_API_KEY=pega_aqui_tu_clave_personal
```

`.env` está excluido de Git para que cada desarrollador use su propia clave. El repositorio solo incluye `.env.example`, que no contiene secretos. Las variables de entorno del sistema tienen prioridad sobre `.env`.

Arranca la aplicación desde esa carpeta:

```powershell
.\mvnw.cmd spring-boot:run
```

La aplicación queda disponible en `http://localhost:8080`.

Las cuatro demostraciones hacen peticiones a rutas bajo `https://api.nasa.gov`. Si no defines la clave, se usa `DEMO_KEY`, cuya cuota por IP es reducida y compartida. Para ejecutar los tests:

```powershell
.\mvnw.cmd clean test
```

## Demostraciones

| Ruta | API | Qué se aprende |
|---|---|---|
| `/techtransfer` | TechTransfer | Buscar patentes NASA con el término de búsqueda del endpoint `/techtransfer/patent/` y examinar su respuesta JSON real. |
| `/asteroids` | NeoWs | Filtrar por fechas, validar el intervalo máximo de siete días y recorrer JSON agrupado por fecha. Requiere `NASA_API_KEY`. |
| `/donki` | DONKI FLR | Consultar fulguraciones solares en un intervalo y leer una colección JSON de eventos. Requiere `NASA_API_KEY`. |
| `/epic` | EPIC Natural | Consultar metadatos de las últimas imágenes de la Tierra o de una fecha concreta. Requiere `NASA_API_KEY`. |

APOD se explica como servicio relacionado en `infoapi.md`, pero no se usa como demo porque NASA está migrando su ruta y la nueva versión reside en otro host.

El catálogo NASA también enlaza con servicios relacionados alojados en otros dominios, como EONET, NASA Images, GIBS o Exoplanet Archive. Se pueden estudiar como ejemplos de ecosistema, pero **no se llaman desde esta aplicación**: el alcance del proyecto es practicar peticiones al host `api.nasa.gov`.

## Recorrido MVC

1. El navegador envía un `GET` con filtros; Thymeleaf genera HTML en el servidor.
2. Una clase `web/*Controller` valida la entrada y decide qué datos pedir.
3. Una clase `nasa/*Service` encapsula la URL, los parámetros y los errores del servicio NASA.
4. `NasaModels` contiene DTOs pequeños con los campos usados por las vistas.
5. El controlador añade los datos al `Model` y devuelve una plantilla de `src/main/resources/templates`.

Los clientes HTTP tienen timeouts finitos. Las claves se leen desde variables de entorno, no se escriben en las plantillas ni se incluyen en los logs. NASA puede aplicar cuotas o cambiar sus respuestas; la aplicación comunica los fallos sin fingir que los datos están disponibles.
