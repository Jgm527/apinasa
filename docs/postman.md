# Postman

Postman es una herramienta para hacer peticiones HTTP sin escribir código. Sirve para probar APIs, ver las respuestas al momento y guardar consultas repetidas. No forma parte del proyecto: es un complemento para explorar las APIs antes de llamarlas desde el programa.

## 1. Qué es

Un cliente HTTP con interfaz gráfica. En lugar de escribir `curl` o código, rellenas el método, la URL y los parámetros, pulsas **Send** y ves la respuesta. Es gratis para uso individual y se descarga de [postman.com](https://www.postman.com/).

## 2. Cómo funciona

Postman hace lo mismo que tu programa Java: manda una petición a un servidor y recibe una respuesta.

```text
Tú (Postman)  ──  petición  ──▶  servidor de NASA
              ◀──  respuesta  ──
```

- **La petición** lleva un método (`GET` para pedir datos), una URL y, si hace falta, parámetros.
- **La respuesta** trae un código de estado (`200` es que fue bien), cabeceras y un cuerpo, que aquí es JSON.

En la pantalla de Postman verás tres zonas:

| Zona | Qué hay |
|---|---|
| Izquierda | Tus colecciones: grupos de peticiones guardadas. |
| Arriba | La petición: método, URL y botón **Send**. |
| Abajo | La respuesta: **Body**, **Headers** y el código de estado. |

Con eso ya puedes explorar una API. El resto de esta página son extras: guardar la clave en una variable, importar las peticiones del proyecto y ver la respuesta como una tabla.

## 3. Para qué se usa

| Uso | Ejemplo en este proyecto |
|---|---|
| Probar un endpoint | Comprobar EPIC antes de codificar la llamada. |
| Depurar | Ver el código de estado y el cuerpo real de la respuesta. |
| Repetir peticiones | Guardar la consulta de asteroides con sus fechas. |
| Guardar claves | Tener `api_key` en una variable, no en cada URL. |
| Compartir | Enviar una colección con la petición ya configurada. |

## 4. Tu primera petición

1. Descarga Postman e instálalo.
2. Crea una petición: elige el método `GET` y pega la URL.
3. Pulsa **Send**.

```text
GET https://epic.gsfc.nasa.gov/api/natural
```

La respuesta aparece separada en pestañas: **Body** (el JSON), **Headers** (las cabeceras) y el código de estado (`200`, `4xx`, `429`...). El cuerpo se formatea y colorea automáticamente.

> Prueba NeoWs con un rango de hasta siete días y ajusta las fechas a un periodo válido:
> `GET https://api.nasa.gov/neo/rest/v1/feed?start_date=2026-09-26&end_date=2026-10-02&api_key=DEMO_KEY`

## 5. La clave en una variable

Solo algunas APIs piden clave, como NeoWs. Escribirla en cada petición es incómodo y fácil de copiar mal. Crea un entorno (Environment) con una variable `NASA_API_KEY` y úsala en la URL:

```text
GET https://api.nasa.gov/neo/rest/v1/feed?start_date=2026-09-26&end_date=2026-10-02&api_key={{NASA_API_KEY}}
```

Postman sustituye `{{NASA_API_KEY}}` por el valor del entorno al enviar, así la clave real no aparece en la petición guardada. No compartas colecciones ni entornos que contengan tu clave personal: trátala como el `.env` local.

## 6. Importar la colección del proyecto

En la carpeta [`postman/`](https://github.com/Jgm527/apinasa/tree/main/postman) hay una colección y un entorno con las variables. La colección tiene una petición por API (TechTransfer, NeoWs, DONKI y EPIC) y otra a `ejemplo-rest`, que solo funciona con esa app arrancada.

1. En Postman, pulsa **Import** y arrastra los dos archivos de la carpeta.
2. Elige el entorno **NASA** arriba a la derecha.
3. Abre una petición y pulsa **Send**.

El entorno trae `DEMO_KEY`, que sirve para probar pero tiene límites estrictos. Para usar tu clave, cámbiala en tu copia y no exportes ni subas ese entorno.

## 7. Postman y la aplicación

Postman no se conecta con `apps/apinasa`: la aplicación llama a NASA desde Java con `RestClient`. Postman ayuda a entender qué responde NASA para utilizar esa respuesta en el código. Para probar la propia aplicación, usa el navegador en <http://localhost:8080>.

## 8. Ver la respuesta como una tabla (Visualizer)

El JSON en bruto es difícil de leer. El Visualizer dibuja la respuesta con una plantilla HTML dentro de Postman.

1. Abre la petición y ve a la pestaña **Scripts**, apartado **Post-response**.
2. Escribe la plantilla y pasa los datos con `pm.visualizer.set`:

```js
const plantilla = `
  <style>th, td { padding: 4px 16px; text-align: left; }</style>
  <table>
    <tr><th>Imagen</th><th>Fecha</th></tr>
    {{#each imagenes}}
      <tr><td>{{identifier}}</td><td>{{date}}</td></tr>
    {{/each}}
  </table>`;

pm.visualizer.set(plantilla, { imagenes: pm.response.json() });
```

3. Pulsa **Send** y, en la respuesta, la pestaña **Visualize**.

Este ejemplo es el de la petición **EPIC: últimas imágenes** de la colección, que ya trae el script puesto. EPIC devuelve una lista de objetos, por eso la plantilla recorre `imagenes` con `#each`. Otra API devuelve el JSON con otra forma, así que hay que ajustar qué parte se pasa a la plantilla.

## Glosario

- **Cliente HTTP**: programa que envía peticiones y recibe respuestas.
- **Colección**: conjunto de peticiones guardadas en Postman.
- **Entorno**: grupo de variables (como la clave) que se aplica a las peticiones.
- **Variable**: valor reutilizable que se escribe `{{nombre}}`.
- **Send**: botón que ejecuta la petición.
- **Body**: cuerpo de la respuesta, aquí JSON.
- **Visualizer**: función de Postman que dibuja la respuesta con una plantilla HTML.

## Siguiente paso

Repite la misma petición con `curl` en [tu primera petición](primera-peticion.md) si aún no lo has hecho y consulta todas las APIs disponibles en [las APIs de NASA](apis.md).
