# Tu primera petición

Para usar una API de NASA hace falta una URL, una clave y saber leer lo que devuelve. En esta página haces una petición real de principio a fin.

## 1. Pide tu clave

Entra en [api.nasa.gov](https://api.nasa.gov/), rellena el formulario con tu nombre y tu correo y recibirás una clave personal. Es gratis y llega al momento.

Mientras no la tengas puedes usar `DEMO_KEY`. Sirve para probar, pero solo permite 30 peticiones por hora y 50 al día por IP, y se agota rápido si varias personas comparten la misma conexión.

## 2. Lanza una petición

Con EPIC, que devuelve imágenes de la Tierra tomadas desde el espacio, la petición es una sola línea:

```bash
curl "https://api.nasa.gov/EPIC/api/natural/images?api_key=DEMO_KEY"
```

Cada parte de la URL tiene su papel:

| Parte | Ejemplo | Qué es |
|---|---|---|
| Host | `api.nasa.gov` | El servidor al que llamas. Todas las APIs de este proyecto usan el mismo. |
| Ruta | `/EPIC/api/natural/images` | Qué recurso pides. |
| Parámetros | `?api_key=DEMO_KEY` | Datos extra que van tras la `?`. Aquí, tu clave. Se separan con `&`. |

## 3. Lee la respuesta

NASA responde con JSON, una lista de objetos. Un objeto de EPIC se parece a esto:

```json
{
  "identifier": "epic_1b_20261001...",
  "caption": "Earth imagery captured by EPIC",
  "image": "epic_1b_20261001...",
  "date": "2026-10-01 12:00:00",
  "centroid_coordinates": { "lat": 0.0, "lon": 0.0 }
}
```

Los campos que más importan son `date`, que dice cuándo se tomó la imagen, e `image`, el nombre del archivo. Algunos campos pueden faltar, así que conviene no darlos por seguros.

## 4. Si algo falla

El código de estado de la respuesta dice qué ha pasado.

| Código | Significa | Qué hacer |
|---|---|---|
| `200` | Todo bien. | Leer el JSON. |
| `4xx` | Algo de la petición está mal: la clave, un parámetro, una fecha. | Revisar la petición. |
| `5xx` | El fallo está en el servidor de NASA. | Probar más tarde. |
| `429` | Has superado la cuota. | Esperar, o usar tu clave personal. |

> [!TIP]
> Un `429` no significa que tu código esté mal. Es solo que se ha agotado la cuota de la clave.

## Glosario

- **API**: una forma de pedirle datos a otro programa por internet.
- **Endpoint**: una dirección concreta de una API, como `/EPIC/api/natural/images`.
- **Query parameter**: un dato que se añade a la URL tras la `?`.
- **API key**: la clave que identifica quién hace la petición.
- **JSON**: el formato de texto en el que responde la API.
- **Cuota**: cuántas peticiones puedes hacer en un tiempo.

## Siguiente paso

Mira cómo se consumen estas APIs desde Java en [las APIs de NASA](/docs/apis) y en los [ejemplos mínimos](/docs/ejemplos).
