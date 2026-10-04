# Instalación y ejecución

## Requisitos

- Java 25. Los tres proyectos declaran `java.version=25`.
- Conexión a Internet para llamar a NASA.
- Node 20 o superior, solo si quieres arrancar la landing.

No hace falta instalar Maven: cada proyecto trae su `mvnw`.

## Clave de NASA

Pide una clave gratuita en [api.nasa.gov](https://api.nasa.gov/). Sin clave se usa `DEMO_KEY`, que permite 30 peticiones por hora y 50 al día por IP. Basta para probar, pero se agota rápido.

La clave se lee de la variable de entorno `NASA_API_KEY`. En `apps/apinasa` también puedes ponerla en un `.env` local:

```bash
cd apps/apinasa
cp .env.example .env
# edita .env y cambia el valor de NASA_API_KEY
```

`.env` está en `.gitignore`. Nunca subas tu clave al repositorio.

## Ejecutar la aplicación completa

```bash
cd apps/apinasa
./mvnw spring-boot:run      # en Windows: .\mvnw.cmd spring-boot:run
```

Se abre en <http://localhost:8080>. Para los tests:

```bash
./mvnw clean test
```

## Ejecutar los ejemplos

Los dos escuchan en el puerto 8080, así que arranca uno cada vez, o cambia el puerto con `PORT=8081`.

```bash
cd apps/ejemplo-mvc    # o apps/ejemplo-rest
NASA_API_KEY=tu_clave ./mvnw spring-boot:run
```

## Ejecutar la landing

```bash
cd landing
npm install
npm run dev
```
