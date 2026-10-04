<script setup lang="ts">
import { computed } from 'vue'
import { RouterLink } from 'vue-router'
import { Card } from '@joseestevez/vue-elastic-ui'
import { ArrowLeft, ArrowRight } from '@lucide/vue'
import { docPages } from './docs'

// At the end of a docs page: the one before and the one after, to keep reading without going
// back to the sidebar.
const props = defineProps<{ slug: string }>()
const i = computed(() => docPages.findIndex((p) => p.slug === props.slug))
const previous = computed(() => docPages[i.value - 1])
const next = computed(() => docPages[i.value + 1])
</script>

<template>
  <nav aria-label="Anterior y siguiente" class="mt-16 flex flex-col gap-3">
    <span class="text-xs text-fg-muted">{{ i + 1 }} de {{ docPages.length }}</span>
    <div class="grid gap-4 sm:grid-cols-2">
      <RouterLink
        v-if="previous"
        :to="`/docs/${previous.slug}`"
        class="group rounded-[var(--radius-xl)] focus-visible:outline-2 focus-visible:outline-accent"
      >
        <Card size="sm" class="h-full gap-1 px-4 transition-colors duration-150 group-hover:border-border-strong">
          <span class="flex items-center gap-1.5 text-xs text-fg-muted">
            <ArrowLeft class="size-3.5 transition-transform duration-150 group-hover:-translate-x-0.5" aria-hidden="true" />
            Anterior
          </span>
          <span class="text-sm font-semibold text-fg">{{ previous.title }}</span>
        </Card>
      </RouterLink>
      <span v-else aria-hidden="true" class="max-sm:hidden" />

      <RouterLink
        v-if="next"
        :to="`/docs/${next.slug}`"
        class="group rounded-[var(--radius-xl)] focus-visible:outline-2 focus-visible:outline-accent"
      >
        <Card size="sm" class="h-full items-end gap-1 px-4 text-right transition-colors duration-150 group-hover:border-border-strong">
          <span class="flex items-center gap-1.5 text-xs text-fg-muted">
            Siguiente
            <ArrowRight class="size-3.5 transition-transform duration-150 group-hover:translate-x-0.5" aria-hidden="true" />
          </span>
          <span class="text-sm font-semibold text-fg">{{ next.title }}</span>
          <span class="text-xs text-fg-secondary">{{ next.description }}</span>
        </Card>
      </RouterLink>
    </div>
  </nav>
</template>
