<script setup lang="ts">
import {
  Button,
  Card,
  CardDescription,
  CardHeader,
  CardTitle,
  IconLink,
  IconLinks,
  Separator,
} from '@joseestevez/vue-elastic-ui'
import { BookOpen, Boxes, LayoutTemplate, Rocket } from '@lucide/vue'
import { siGithub } from 'simple-icons'
import SiteHeader from '../SiteHeader.vue'

const REPO = 'https://github.com/Jgm527/apinasa'

const sections = [
  { id: 'incluye', label: 'Qué incluye' },
  { id: 'apis', label: 'APIs' },
]

const features = [
  { icon: LayoutTemplate, title: 'Aplicación completa', text: 'Spring Boot y Thymeleaf con validación, errores y tests.' },
  { icon: Boxes, title: 'Dos ejemplos mínimos', text: 'Un controlador MVC y otro REST, reducidos a lo esencial.' },
  { icon: BookOpen, title: 'Documentación', text: 'Una guía paso a paso, desde pedir la clave hasta leer el JSON.' },
]

const apis = [
  { name: 'Asteroids NeoWs', text: 'Asteroides cercanos a la Tierra' },
  { name: 'TechTransfer', text: 'Patentes de NASA' },
  { name: 'DONKI', text: 'Fulguraciones solares' },
  { name: 'EPIC', text: 'Imágenes de la Tierra' },
]
</script>

<template>
  <SiteHeader :links="sections" :repo="REPO" />

  <main class="mx-auto w-[min(100%-2rem,64rem)]">
    <section class="grid items-center gap-12 py-24 md:grid-cols-[1.4fr_1fr]">
      <div class="flex flex-col items-start gap-6">
        <h1 class="text-4xl font-semibold tracking-tight text-fg sm:text-5xl">
          Aprende Spring Boot con las APIs de NASA
        </h1>
        <p class="max-w-xl text-lg text-fg-secondary">
          Una guía práctica para consumir APIs reales desde Java, con una aplicación completa y dos ejemplos para
          empezar.
        </p>
        <div class="flex flex-wrap gap-3 pt-2">
          <Button to="/docs/primera-peticion" :icon="Rocket">Tu primera petición</Button>
          <Button to="/docs" variant="outline">Documentación</Button>
        </div>
      </div>
      <img src="/logo.svg" alt="Logo de NASA Spring Guide" class="mx-auto w-full max-w-xs" />
    </section>

    <section id="incluye" class="scroll-mt-20 py-16">
      <div class="grid gap-5 sm:grid-cols-3">
        <Card v-for="f in features" :key="f.title">
          <CardHeader>
            <component :is="f.icon" class="mb-2 size-5 text-fg-muted" aria-hidden="true" />
            <CardTitle>{{ f.title }}</CardTitle>
            <CardDescription>{{ f.text }}</CardDescription>
          </CardHeader>
        </Card>
      </div>
    </section>

    <section id="apis" class="scroll-mt-20 py-16">
      <h2 class="mb-8 text-2xl font-semibold tracking-tight text-fg">Cuatro APIs de NASA</h2>
      <div class="grid gap-5 sm:grid-cols-2 lg:grid-cols-4">
        <Card v-for="api in apis" :key="api.name" size="sm">
          <CardHeader>
            <CardTitle>{{ api.name }}</CardTitle>
            <CardDescription>{{ api.text }}</CardDescription>
          </CardHeader>
        </Card>
      </div>
    </section>

    <footer class="flex flex-col gap-8 pt-16 pb-12">
      <Separator />
      <div class="flex flex-wrap items-center justify-between gap-4 text-sm text-fg-muted">
        <span>Proyecto educativo, sin relación oficial con NASA.</span>
        <IconLinks variant="ghost" label="Enlaces del proyecto" class="w-32 justify-end">
          <IconLink :icon="siGithub" label="GitHub" :href="REPO" />
        </IconLinks>
      </div>
    </footer>
  </main>
</template>
