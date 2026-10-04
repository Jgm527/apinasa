<script setup lang="ts">
import { Button, IconLink, IconLinks, ThemeToggle } from '@joseestevez/vue-elastic-ui'
import { siGithub } from 'simple-icons'
import { RouterLink } from 'vue-router'

defineProps<{ links: { id: string; label: string }[]; repo: string }>()

const go = (id: string) => document.getElementById(id)?.scrollIntoView({ behavior: 'smooth' })
</script>

<template>
  <!-- Held at the top; the page's colour at 70% over a blur, so content passes softly behind it
       and nothing is ever moved to make room for it. -->
  <header class="sticky top-0 z-40 border-b border-border bg-bg/70 backdrop-blur-md backdrop-saturate-150">
    <div class="mx-auto flex h-14 w-[min(100%-2rem,64rem)] items-center justify-between gap-4">
      <RouterLink to="/" class="flex items-center gap-2 font-semibold tracking-tight text-fg">
        <img src="/logo.svg" alt="" class="h-6 w-auto" />
        NASA Spring Guide
      </RouterLink>
      <nav class="hidden items-center gap-1 md:flex" aria-label="Secciones">
        <Button v-for="link in links" :key="link.id" variant="ghost" size="sm" @click="go(link.id)">
          {{ link.label }}
        </Button>
        <Button to="/docs" variant="ghost" size="sm">Documentación</Button>
      </nav>
      <div class="flex items-center gap-1">
        <IconLinks variant="ghost" label="Enlaces del proyecto" class="w-32 justify-end">
          <IconLink :icon="siGithub" label="GitHub" :href="repo" />
        </IconLinks>
        <ThemeToggle />
      </div>
    </div>
  </header>
</template>
