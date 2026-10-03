# APIs de NASA con Java y Spring Boot

**Revisada el 2 de octubre de 2026.** Esta guía se centra en el catálogo de [api.nasa.gov](https://api.nasa.gov/) y acompaña a la aplicación `ApiNasa/apinasa`. Las peticiones externas que hace el proyecto se dirigen exclusivamente a `https://api.nasa.gov`.

## Alcance del proyecto

`api.nasa.gov` es el portal central de NASA para publicar un catálogo de APIs y ofrecer acceso común mediante claves y límites. Algunos servicios son pasarelas a sistemas de distintos centros, pero la aplicación cliente se conecta al host central. La app no llama a otros hosts de API.

Las cuatro demos de la web son:

| Ruta local | API | Ruta externa | Qué practicar |
|---|---|---|---|
| `/techtransfer` | TechTransfer | `/techtransfer/patent/` | Buscar patentes y explorar una respuesta JSON de estructura flexible |
| `/asteroids` | Asteroids NeoWs | `/neo/rest/v1/feed` | Query parameters, rango de fechas y JSON agrupado por fecha |
| `/donki` | DONKI, producto FLR | `/DONKI/FLR` | Colecciones JSON, parámetros camelCase y datos científicos opcionales |
| `/epic` | EPIC Natural | `/EPIC/api/natural/images` o `/EPIC/api/natural/date/{fecha}` | Consultar metadatos de imágenes recientes o de una fecha |

En todos los casos el host externo es `api.nasa.gov`. El código del navegador solicita una ruta local; los servicios Spring ejecutan la llamada de NASA en el backend. Las páginas muestran la URL realmente enviada, el estado, `Content-Type` y el cuerpo JSON de esa respuesta. La clave se redacta tanto en la URL como si la propia respuesta la repite en alguno de sus enlaces.

## Cómo se obtiene acceso

Se puede pedir una clave personal gratuita en el [portal de NASA APIs](https://api.nasa.gov/). La aplicación la lee de `NASA_API_KEY`; en local puede definirse en un `.env` ignorado por Git, o en una variable de entorno del sistema.

- Límite general publicado para una clave personal: **1.000 peticiones por hora**, compartidas entre los servicios que pasan por `api.nasa.gov`.
- `DEMO_KEY`: **30 peticiones por hora y 50 al día por IP**. Es solo para pruebas ligeras.
- Algunas respuestas contienen las cabeceras `X-RateLimit-Limit` y `X-RateLimit-Remaining`.
- `429 Too Many Requests` indica una limitación de cuota, no necesariamente un error de código o una ruta mal escrita.
- Que el acceso no tenga coste por petición no significa uso ilimitado, disponibilidad garantizada o contrato estable.

## Anatomía de una petición HTTP

Ejemplo de NeoWs con parámetros de consulta:

```text
GET https://api.nasa.gov/neo/rest/v1/feed?start_date=2026-09-26&end_date=2026-10-02&api_key=...
Accept: application/json
```

- **Método:** `GET` solicita datos sin modificar el recurso.
- **Host:** `api.nasa.gov`, el destino al que se conecta la aplicación.
- **Ruta:** el recurso específico, en el ejemplo `/neo/rest/v1/feed`.
- **Query parameters:** se añaden tras `?` y se separan con `&`; sirven para filtrar o autenticar.
- **Cabecera `Accept`:** indica el formato que solicita el cliente.
- **Respuesta HTTP:** combina un código de estado, cabeceras y un cuerpo. En estas APIs el cuerpo suele ser JSON.

El backend valida primero los parámetros locales; después el servicio construye la URI con un `UriBuilder`, ejecuta la llamada usando `RestClient` y deserializa el JSON. Así no se envía la clave privada al navegador.

## 1. TechTransfer: búsqueda de patentes

TechTransfer ofrece búsquedas de patentes, software y resultados de transferencia tecnológica. La aplicación demuestra la búsqueda de patentes por una palabra:

```text
GET https://api.nasa.gov/techtransfer/patent/?aircraft&api_key=...
```

El término de búsqueda se representa como un parámetro sin valor, siguiendo el ejemplo publicado en la ficha del catálogo. Hay rutas separadas para patentes, software y spinoffs. Las respuestas contienen JSON propio del catálogo, con resultados y metadatos. Como su estructura depende del producto, la demo muestra el cuerpo real y lo mantiene flexible en vez de inventar un DTO para campos que no utiliza.

La página local `/techtransfer` valida que el texto no esté vacío y limita su longitud antes de consultar.

## 2. Asteroids NeoWs: objetos cercanos a la Tierra

NeoWs consulta asteroides y aproximaciones cercanas:

```text
GET https://api.nasa.gov/neo/rest/v1/feed?start_date=2026-09-26&end_date=2026-10-02&api_key=...
```

El feed permite un rango máximo de siete días. El JSON agrupa los objetos por fecha:

```json
{
  "near_earth_objects": {
    "2026-10-02": [
      {
        "id": "2465633",
        "name": "465633 (2009 JR5)",
        "is_potentially_hazardous_asteroid": false,
        "close_approach_data": [
          {
            "close_approach_date": "2026-10-02",
            "miss_distance": {
              "kilometers": "...",
              "astronomical": "..."
            }
          }
        ]
      }
    ]
  }
}
```

`near_earth_objects` es un mapa cuya clave es la fecha y cuyo valor es una lista. La distancia puede aparecer en varias unidades; no compares kilómetros con unidades astronómicas como si fueran lo mismo. “Potencialmente peligroso” es una clasificación, no un aviso de impacto inminente.

## 3. DONKI FLR: fulguraciones solares

DONKI contiene distintos productos de meteorología espacial. La app consulta **FLR** (Solar Flares):

```text
GET https://api.nasa.gov/DONKI/FLR?startDate=2026-09-26&endDate=2026-10-02&api_key=...
```

En este producto los parámetros de fecha se escriben en camelCase: `startDate` y `endDate`. El resultado es una lista de eventos. Un registro puede contener campos como:

```json
[
  {
    "flrID": "2026-10-02T...-FLR-001",
    "beginTime": "2026-10-02T...",
    "peakTime": "2026-10-02T...",
    "endTime": "2026-10-02T...",
    "classType": "M1.2",
    "sourceLocation": "N15E...",
    "activeRegionNum": 12345,
    "instruments": [
      { "id": 1, "displayName": "GOES-P: ..." }
    ]
  }
]
```

Los campos opcionales pueden faltar. DONKI ofrece análisis y datos de investigación; no se presenta aquí como sistema de emergencia o pronóstico operativo. La app limita su consulta didáctica a 30 días.

## 4. EPIC Natural: metadatos de imágenes de la Tierra

EPIC proporciona metadatos de imágenes de disco completo de la Tierra captadas por DSCOVR. Para obtener las entradas más recientes:

```text
GET https://api.nasa.gov/EPIC/api/natural/images?api_key=...
```

Para una fecha concreta, la fecha forma parte de la ruta:

```text
GET https://api.nasa.gov/EPIC/api/natural/date/2026-10-01?api_key=...
```

El cuerpo es una lista JSON. Un objeto incluye datos como:

```json
[
  {
    "identifier": "epic_1b_20261001...",
    "caption": "Earth imagery captured by EPIC",
    "image": "epic_1b_20261001...",
    "date": "2026-10-01 12:00:00",
    "version": "03",
    "centroid_coordinates": { "lat": 0.0, "lon": 0.0 }
  }
]
```

La pantalla enseña esos metadatos y el intercambio JSON. El nombre del archivo (`image`) identifica el recurso asociado; esta demo se centra en la respuesta de metadatos y mantiene la petición bajo `api.nasa.gov`.

## Usar `RestClient` desde Spring Boot

El patrón básico es crear la URI de forma segura, incluir los parámetros y convertir el JSON a un DTO:

```java
URI requestUri = UriComponentsBuilder
    .fromUriString("https://api.nasa.gov/neo/rest/v1/feed")
    .queryParam("start_date", startDate)
    .queryParam("end_date", endDate)
    .queryParam("api_key", apiKey)
    .encode()
    .build()
    .toUri();

NeoFeed feed = restClient.get()
    .uri(requestUri)
    .accept(MediaType.APPLICATION_JSON)
    .retrieve()
    .body(NeoFeed.class);
```

En esta aplicación `NasaHttpSupport` amplía ese patrón: lee y conserva el cuerpo HTTP bruto, registra estado y `Content-Type`, parsea el mismo JSON a un DTO y redacta la clave. Si NASA responde con `4xx`, `5xx` o `429`, el intercambio sigue disponible para mostrarlo en la página.

## Flujo MVC de la aplicación

1. El navegador envía un `GET` a una ruta local, por ejemplo `/asteroids`.
2. El controlador comprueba fechas, rangos y búsquedas antes de consumir cuota.
3. El servicio Java construye una URI que empieza por `https://api.nasa.gov`.
4. `RestClient` solicita la respuesta; `NasaHttpSupport` guarda el intercambio y convierte el JSON.
5. El controlador añade el resultado y los datos HTTP al `Model`.
6. Thymeleaf presenta los datos funcionales y permite inspeccionar el cuerpo real recibido.

### Buenas prácticas

1. Conserva la clave en `.env` local o en un gestor de secretos; no la subas a Git ni la envíes al frontend.
2. Construye query parameters con `UriComponentsBuilder` para codificar caracteres correctamente.
3. Valida intervalos y longitudes antes de llamar a NASA.
4. Configura timeouts, controla `4xx`/`5xx` y no repitas `429` en un bucle.
5. Usa caché si vuelves a solicitar datos que no cambian; respeta las unidades y el contexto científico.
6. En tests, sustituye HTTP por respuestas mock para que no dependan de la disponibilidad de NASA.

## Servicios relacionados fuera del alcance

El catálogo y el ecosistema NASA también enlazan EONET, NASA Image and Video Library, GIBS, Exoplanet Archive y JPL SSD/CNEOS. Son servicios válidos para otros trabajos, pero usan hosts y contratos distintos. Se mencionan como contexto; **no forman parte de las llamadas ni de los ejemplos Java de esta práctica**.

APOD también está en transición: el catálogo ha anunciado la retirada del endpoint clásico de `api.nasa.gov/planetary/apod` para el 1 de diciembre de 2026 y publica un feed nuevo en otro host. Se omite de la aplicación para no enseñar una ruta que está por retirarse ni introducir una excepción al alcance.

## Fuentes

- [Portal api.nasa.gov: catálogo, claves y límites](https://api.nasa.gov/)
- [TechTransfer](https://api.nasa.gov/#techtransfer)
- [Asteroids NeoWs](https://api.nasa.gov/#neo_ws)
- [DONKI](https://api.nasa.gov/#donki)
- [EPIC](https://api.nasa.gov/#epic)
- [README de la aplicación](ApiNasa/apinasa/README.md)
