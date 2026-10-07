<script setup lang="ts">
import { computed, ref, type Component } from 'vue'
import { AnimatedList, Input, ToggleGroup, ToggleGroupItem } from '@joseestevez/vue-elastic-ui'
import { Atom, Code, HeartPulse, Plane, Radar, Search } from '@lucide/vue'
import { patentCategories, patents } from './data'

type Category = (typeof patentCategories)[number]
const look: Record<Category, { icon: Component; colour: string }> = {
  Materiales: { icon: Atom, colour: 'var(--ex-cat-materiales)' },
  Sensores: { icon: Radar, colour: 'var(--ex-cat-sensores)' },
  Aeronáutica: { icon: Plane, colour: 'var(--ex-cat-aeronautica)' },
  Software: { icon: Code, colour: 'var(--ex-cat-software)' },
  Salud: { icon: HeartPulse, colour: 'var(--ex-cat-salud)' },
}
const lookOf = (c: string) => look[c as Category]

const query = ref('')
const category = ref<string>('')
const filtered = computed(() => {
  const q = query.value.trim().toLowerCase()
  return patents.filter(
    (p) => (!category.value || p.category === category.value) && (!q || p.title.toLowerCase().includes(q)),
  )
})
// Before any search, a handful is enough to see what comes back.
const found = computed(() => (query.value.trim() || category.value ? filtered.value : filtered.value.slice(0, 6)))

/** The title cut round the searched word, so it can be marked. */
function parts(title: string) {
  const q = query.value.trim()
  const i = q ? title.toLowerCase().indexOf(q.toLowerCase()) : -1
  return i < 0 ? [title, '', ''] : [title.slice(0, i), title.slice(i, i + q.length), title.slice(i + q.length)]
}
</script>

<template>
  <div class="flex flex-col gap-4">
    <div class="flex flex-wrap items-center gap-3">
      <Input v-model="query" :icon="Search" placeholder="Busca una palabra: sensor, aviones…" aria-label="Buscar patentes" class="w-full sm:w-72" />
      <ToggleGroup
        type="single"
        size="sm"
        :model-value="category"
        aria-label="Categoría"
        class="flex-wrap"
        @update:model-value="category = (($event as string | undefined) ?? '')"
      >
        <ToggleGroupItem v-for="c in patentCategories" :key="c" :value="c">
          <span class="size-2 rounded-full" :style="{ background: look[c].colour }" aria-hidden="true" />
          {{ c }}
        </ToggleGroupItem>
      </ToggleGroup>
    </div>

    <p class="text-sm text-fg-muted" aria-live="polite">
      {{ filtered.length }} {{ filtered.length === 1 ? 'patente' : 'patentes' }} en <code class="font-mono text-xs">results</code>
    </p>

    <AnimatedList :items="found" :item-key="(p) => p.id" class="grid gap-3 sm:grid-cols-2">
      <template #default="{ item }">
        <div
          class="flex h-full items-start gap-3 rounded-[var(--radius-md)] p-4"
          :style="{ background: `color-mix(in oklab, ${lookOf(item.category).colour} 9%, var(--color-bg))` }"
        >
          <span
            class="grid size-9 shrink-0 place-items-center rounded-[var(--radius-sm)] text-white"
            :style="{ background: lookOf(item.category).colour }"
          >
            <component :is="lookOf(item.category).icon" class="size-4.5" aria-hidden="true" />
          </span>
          <div class="flex min-w-0 flex-col gap-1">
            <span class="leading-snug text-fg">
              {{ parts(item.title)[0] }}<mark v-if="parts(item.title)[1]" class="rounded-sm bg-[color-mix(in_oklab,var(--ex-flare-c)_45%,transparent)] px-0.5 text-fg">{{ parts(item.title)[1] }}</mark>{{ parts(item.title)[2] }}
            </span>
            <span class="flex items-center gap-2 text-xs text-fg-muted">
              <span class="font-mono">{{ item.id }}</span>
              <span>·</span>
              <span :style="{ color: `color-mix(in oklab, ${lookOf(item.category).colour} 75%, var(--color-fg))` }">{{ item.category }}</span>
            </span>
          </div>
        </div>
      </template>
      <template #empty>
        <p class="py-6 text-center text-fg-muted">Ninguna patente contiene «{{ query }}».</p>
      </template>
    </AnimatedList>
  </div>
</template>
