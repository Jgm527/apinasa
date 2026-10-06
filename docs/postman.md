# Postman

Postman es una herramienta para hacer peticiones HTTP sin escribir código. Sirve para probar APIs, ver las respuestas al momento y guardar consultas repetidas. No forma parte del proyecto: es un complemento para explorar las APIs antes de llamarlas desde el programa.

## 1. Qué es

Un cliente HTTP con interfaz gráfica. En lugar de escribir `curl` o código, rellenas el método, la URL y los parámetros, pulsas **Send** y ves la respuesta. Es gratis para uso individual y se descarga de [postman.com](https://www.postman.com/).

## 2. Para qué se usa

| Uso | Ejemplo en este proyecto |
|---|---|
| Probar un endpoint | Comprobar EPIC antes de codificar la llamada. |
| Depurar | Ver el código de estado y el cuerpo real de la respuesta. |
| Repetir peticiones | Guardar la consulta de asteroides con sus fechas. |
| Guardar claves | Tener `api_key` en una variable, no en cada URL. |
| Compartir | Enviar una colección con la petición ya configurada. |

## 3. Tu primera petición

1. Descarga Postman e instálalo.
2. Crea una petición: elige el método `GET` y pega la URL.
3. Pulsa **Send**.

```text
GET https://api.nasa.gov/EPIC/api/natural/images?api_key=DEMO_KEY
```

La respuesta aparece separada en pestañas: **Body** (el JSON), **Headers** (las cabeceras) y el código de estado (`200`, `4xx`, `429`...). El cuerpo se formatea y colorea automáticamente.

> [!TIP]
> Prueba NeoWs con un rango de fechas para ver cómo van los query parameters:
> `GET https://api.nasa.gov/neo/rest/v1/feed?start_date=2026-09-26&end_date=2026-10-02&api_key=DEMO_KEY`

## 4. La clave en una variable

Escribir la clave en cada petición es incómodo y fácil de copiar mal. Crea un entorno (Environment) con una variable `NASA_API_KEY` y úsala en la URL:

```text
GET https://api.nasa.gov/EPIC/api/natural/images?api_key={{NASA_API_KEY}}
```

Postman sustituye `{{NASA_API_KEY}}` por el valor del entorno al enviar, así la clave real no aparece en la petición guardada. No compartas colecciones ni entornos que contengan tu clave personal: trátala como el `.env` local.

## 5. Postman y la aplicación

Postman no se conecta con `apps/apinasa`: la aplicación llama a NASA desde Java con `RestClient`. Postman ayuda a entender qué responde NASA para utilizar esa respuesta en el código. Para probar la propia aplicación, usa el navegador en <http://localhost:8080>.

## Glosario

- **Cliente HTTP**: programa que envía peticiones y recibe respuestas.
- **Colección**: conjunto de peticiones guardadas en Postman.
- **Entorno**: grupo de variables (como la clave) que se aplica a las peticiones.
- **Variable**: valor reutilizable que se escribe `{{nombre}}`.
- **Send**: botón que ejecuta la petición.
- **Body**: cuerpo de la respuesta, aquí JSON.

## Siguiente paso

Repite la misma petición con `curl` en [tu primera petición](primera-peticion.md) si aún no lo has hecho y consulta todas las APIs disponibles en [las APIs de NASA](apis.md).
