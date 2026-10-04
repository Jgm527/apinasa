<script setup lang="ts">
import {
  Badge,
  Button,
  Callout,
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
  CodeBlock,
  Diagram,
  DiagramArrow,
  DiagramChip,
  DiagramGroup,
  IconLink,
  IconLinks,
  Separator,
  ThemeToggle,
} from '@joseestevez/vue-elastic-ui'
import { BookOpen, Globe, Layers, Server, FileJson, LayoutTemplate } from '@lucide/vue'
import { siGithub } from 'simple-icons'
import SiteHeader from './SiteHeader.vue'

const REPO = 'https://github.com/Jgm527/apinasa'
const DOCS = `${REPO}/tree/main/docs`

const sections = [
  { href: '#aprender', label: 'Qué se aprende' },
  { href: '#apis', label: 'APIs' },
  { href: '#proyectos', label: 'Proyectos' },
  { href: '#ejecutar', label: 'Ejecutar' },
]

const apis = [
  {
    name: 'Asteroids NeoWs',
    route: '/asteroids',
    text: 'Query parameters, rango de fechas y JSON agrupado por día.',
  },
  {
    name: 'TechTransfer',
    route: '/techtransfer',
    text: 'Búsqueda de patentes y respuestas JSON de estructura flexible.',
  },
  {
    name: 'DONKI',
    route: '/donki',
    text: 'Fulguraciones solares: colecciones de eventos y parámetros en camelCase.',
  },
  {
    name: 'EPIC',
    route: '/epic',
    text: 'Metadatos de las imágenes de la Tierra, las más recientes o las de una fecha.',
  },
]

const projects = [
  { name: 'apinasa', text: 'La aplicación completa: Thymeleaf, validación, gestión de errores y tests.' },
  { name: 'ejemplo-mvc', text: 'Un @Controller y una plantilla, reducidos al mínimo.' },
  { name: 'ejemplo-rest', text: 'Un @RestController que devuelve JSON.' },
]

const learn = [
  'Cómo recorre una petición el patrón MVC: controlador, servicio, DTO y plantilla.',
  'Llamar a una API externa con RestClient y construir la URL con UriComponentsBuilder.',
  'Validar parámetros y tratar los errores 4xx, 5xx y 429 sin romper la página.',
  'Guardar la API key fuera del código y no mostrarla en la respuesta.',
  'Probar sin red con MockRestServiceServer.',
]

const run = `cd apps/apinasa
cp .env.example .env   # pon tu clave en NASA_API_KEY
./mvnw spring-boot:run`
</script>

<template>
  <SiteHeader :links="sections" :repo="REPO" />

  <main class="prose article pt-20 pb-28 [&_h2]:mt-24 [&_h2]:mb-6">
    <header class="not-prose flex flex-col items-start gap-7 pb-6">
      <Badge variant="outline" :icon="Layers" color="var(--spring)">Spring Boot · Java 25</Badge>
      <h1 class="text-4xl font-semibold tracking-tight text-fg sm:text-5xl">
        Aprende Spring Boot con APIs reales de NASA
      </h1>
      <p class="text-lg text-fg-secondary">
        Una aplicación completa y dos ejemplos mínimos, uno MVC y otro REST. Todo lo que se consulta vive bajo
        <code>api.nasa.gov</code>.
      </p>
      <div class="flex flex-wrap gap-4 pt-2">
        <Button :href="DOCS" :icon="BookOpen">Leer la documentación</Button>
        <Button :href="REPO" variant="outline">Ver en GitHub</Button>
      </div>
    </header>

    <h2 id="aprender">Qué se aprende</h2>
    <ul>
      <li v-for="item in learn" :key="item">{{ item }}</li>
    </ul>

    <p>
      El recorrido de una petición es el mismo en las cuatro demos: el navegador llama a una ruta local y el
      backend se encarga de hablar con NASA.
    </p>
    <div class="not-prose my-10">
      <Diagram label="Una petición va del navegador al controlador, que pide los datos a un servicio que llama a NASA">
        <DiagramGroup>
          <DiagramChip :icon="Globe" color="#52525b">Navegador</DiagramChip>
          <DiagramArrow label="GET /asteroids" />
          <DiagramChip :icon="LayoutTemplate" color="#0b3d91">Controlador</DiagramChip>
          <DiagramArrow />
          <DiagramChip :icon="Server" color="#6db33f">Servicio</DiagramChip>
          <DiagramArrow label="RestClient" />
          <DiagramChip :icon="FileJson" color="#52525b">api.nasa.gov</DiagramChip>
        </DiagramGroup>
      </Diagram>
    </div>
    <p>
      <strong>La clave de NASA nunca llega al navegador:</strong> la llamada sale del servicio y la respuesta se
      redacta antes de pintarla.
    </p>

    <h2 id="apis">APIs disponibles</h2>
    <div class="not-prose my-8 grid grid-cols-1 gap-5 sm:grid-cols-2">
      <Card v-for="api in apis" :key="api.name" size="sm">
        <CardHeader>
          <CardTitle>{{ api.name }}</CardTitle>
          <CardDescription>{{ api.text }}</CardDescription>
        </CardHeader>
        <CardFooter>
          <Badge variant="outline">{{ api.route }}</Badge>
        </CardFooter>
      </Card>
    </div>

    <h2 id="proyectos">Proyectos del repositorio</h2>
    <div class="not-prose my-8 grid grid-cols-1 gap-5 sm:grid-cols-3">
      <Card v-for="p in projects" :key="p.name" size="sm">
        <CardHeader>
          <CardTitle>{{ p.name }}</CardTitle>
          <CardDescription>{{ p.text }}</CardDescription>
        </CardHeader>
      </Card>
    </div>

    <h2 id="ejecutar">Ejecutar</h2>
    <p>Necesitas Java 25 y, mejor, una clave gratuita de <a href="https://api.nasa.gov/">api.nasa.gov</a>.</p>
    <div class="not-prose my-8">
      <CodeBlock :code="run" title="bash" />
    </div>
    <Callout variant="note">
      Sin clave se usa <code>DEMO_KEY</code>: 30 peticiones por hora y 50 al día por IP. Basta para probar.
    </Callout>

    <div class="not-prose mt-28 flex flex-col gap-8">
      <Separator />
      <div class="flex flex-wrap items-center justify-between gap-4 text-sm text-fg-muted">
        <span>Proyecto educativo, sin relación oficial con NASA.</span>
        <IconLinks variant="ghost" label="Enlaces del proyecto" class="w-32 justify-end">
          <IconLink :icon="siGithub" label="GitHub" :href="REPO" />
        </IconLinks>
      </div>
    </div>
  </main>
</template>
