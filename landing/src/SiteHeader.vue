<script setup lang="ts">
import { Button, IconLink, IconLinks, ThemeToggle } from '@joseestevez/vue-elastic-ui'
import { siGithub } from 'simple-icons'

defineProps<{ links: { href: string; label: string }[]; repo: string }>()
</script>

<template>
  <!-- Held at the top; the page's colour at 70% over a blur, so content passes softly behind it
       and nothing is ever moved to make room for it. -->
  <header class="sticky top-0 z-40 border-b border-border bg-bg/70 backdrop-blur-md backdrop-saturate-150">
    <div class="mx-auto flex h-14 w-[min(100%-2rem,64rem)] items-center justify-between gap-4">
      <a href="#" class="flex items-center gap-2 font-semibold tracking-tight text-fg">
        <img src="/logo.svg" alt="" class="h-6 w-auto" />
        NASA Spring Guide
      </a>
      <nav class="hidden items-center gap-1 md:flex" aria-label="Secciones">
        <Button v-for="link in links" :key="link.href" :href="link.href" variant="ghost" size="sm">
          {{ link.label }}
        </Button>
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
