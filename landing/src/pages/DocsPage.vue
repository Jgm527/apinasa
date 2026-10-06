<script setup lang="ts">
import { computed } from 'vue'
import { RouterLink, useRoute } from 'vue-router'
import {
  Breadcrumbs,
  Markdown,
  NavTree,
  NavTreeItem,
  Sidebar,
  SidebarLayout,
  SidebarLayoutHeader,
  TableOfContents,
  ThemeToggle,
  headingsOf,
} from '@joseestevez/vue-elastic-ui'
import { docPages } from '../docs'
import ApiExample from '../examples/ApiExample.vue'
import PageNav from '../PageNav.vue'

const route = useRoute()
const slug = computed(() => (route.params.slug as string | undefined) || 'index')
const page = computed(() => docPages.find((p) => p.slug === slug.value) ?? docPages[0]!)
// Fences in the docs that render as a live example.
const fences = { 'nasa-example': ApiExample }
const items = computed(() => headingsOf(page.value.source))
</script>

<template>
  <SidebarLayout>
    <Sidebar variant="connected">
      <template #header>
        <RouterLink to="/" class="flex items-center gap-2 rounded-[var(--radius-sm)] px-2.5 text-sm font-semibold focus-ring">
          <img src="/logo.svg" alt="" class="h-5 w-auto" />
          NASA Spring Guide
        </RouterLink>
      </template>
      <NavTree :model-value="page.slug">
        <NavTreeItem v-for="p in docPages" :key="p.slug" :value="p.slug" :to="`/docs/${p.slug}`" :icon="p.icon">
          {{ p.title }}
        </NavTreeItem>
      </NavTree>
    </Sidebar>

    <main class="min-w-0 flex-1">
      <SidebarLayoutHeader seamless>
        <Breadcrumbs :items="[{ label: 'Documentación', to: '/docs' }, { label: page.title }]" />
        <template #end>
          <ThemeToggle />
        </template>
      </SidebarLayoutHeader>
      <div class="mx-auto flex w-[min(100%-2rem,64rem)] items-start gap-12">
        <div class="min-w-0 flex-1 py-10">
          <Markdown :source="page.source" :components="fences" class="article" />
          <PageNav :slug="page.slug" class="article" />
        </div>
        <aside class="sticky top-24 hidden w-52 shrink-0 py-10 xl:block">
          <TableOfContents v-if="items.length" :items="items" title="En esta página" />
        </aside>
      </div>
    </main>
  </SidebarLayout>
</template>
