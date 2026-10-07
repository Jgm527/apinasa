<script setup lang="ts">
import { computed, ref } from 'vue'
import { Stat, StatGroup } from '@joseestevez/vue-elastic-ui'
import { asteroids, asteroidsPerDay, weekDays } from './data'

// The week's asteroids around the Earth. The distance runs on a log scale, from 0.1 to 100 lunar
// distances, so the close ones and the far ones fit together; the angle only spreads them out.
const SIZE = 400
const C = SIZE / 2
const R = 180
const radius = (ld: number) => (R * (Math.log10(ld) + 1)) / 3
const rings = [
  { ld: 1, label: 'Luna · 1 DL' },
  { ld: 10, label: '10 DL' },
  { ld: 100, label: '100 DL' },
]

const dots = asteroids.map((a, i) => {
  const angle = i * 2.399963 - Math.PI / 2 // golden angle, so no two line up
  const r = radius(a.distanceLd)
  return { ...a, x: C + r * Math.cos(angle), y: C + r * Math.sin(angle), size: 2.5 + 2.2 * Math.log10(a.diameterM) }
})

const closest = asteroids.reduce((a, b) => (a.distanceLd < b.distanceLd ? a : b))
const active = ref(closest.name)
const shown = computed(() => dots.find((d) => d.name === active.value)!)
const total = asteroidsPerDay.reduce((a, b) => a + b, 0)
const km = (ld: number) => Math.round(ld * 384_400).toLocaleString('es-ES')
</script>

<template>
  <div class="flex flex-col gap-6">
    <StatGroup>
      <Stat label="Asteroides esta semana" :value="total" :series="asteroidsPerDay" :labels="weekDays" variant="bars" />
      <Stat label="Potencialmente peligrosos" :value="asteroids.filter((a) => a.hazardous).length" />
      <Stat label="El más cercano" :value="`${closest.distanceLd} DL`" :description="closest.name" />
    </StatGroup>

    <div class="grid overflow-hidden rounded-[var(--radius-lg)] bg-[var(--ex-space)] text-[var(--ex-space-text)] md:grid-cols-[1fr_15rem]">
      <svg
        :viewBox="`0 0 ${SIZE} ${SIZE}`"
        class="mx-auto w-full max-w-md"
        role="img"
        aria-label="Radar con la Tierra en el centro y los asteroides de la semana a su distancia: los peligrosos son grandes y no están entre los más cercanos"
      >
        <defs>
          <radialGradient id="ex-earth" cx="40%" cy="35%">
            <stop offset="0%" stop-color="#7fb8ff" />
            <stop offset="100%" stop-color="var(--ex-ocean)" />
          </radialGradient>
          <radialGradient id="ex-glow">
            <stop offset="0%" stop-color="var(--ex-safe)" stop-opacity="0.35" />
            <stop offset="100%" stop-color="var(--ex-safe)" stop-opacity="0" />
          </radialGradient>
          <linearGradient id="ex-sweep" x1="0" y1="0" x2="1" y2="0">
            <stop offset="0%" stop-color="var(--ex-safe)" stop-opacity="0" />
            <stop offset="100%" stop-color="var(--ex-safe)" stop-opacity="0.22" />
          </linearGradient>
        </defs>

        <g
          class="motion-reduce:hidden"
          :style="{ transformOrigin: `${C}px ${C}px`, animation: 'ex-sweep 8s linear infinite' }"
        >
          <path :d="`M${C},${C} L${C + R},${C} A${R},${R} 0 0,0 ${C + R * Math.cos(-0.6)},${C + R * Math.sin(-0.6)} Z`" fill="url(#ex-sweep)" />
        </g>

        <g v-for="ring in rings" :key="ring.ld">
          <circle :cx="C" :cy="C" :r="radius(ring.ld)" fill="none" stroke="var(--ex-space-line)" stroke-dasharray="3 5" />
          <text :x="C + 4" :y="C - radius(ring.ld) - 5" font-size="11" fill="currentColor" opacity="0.6">{{ ring.label }}</text>
        </g>
        <line :x1="C - R" :y1="C" :x2="C + R" :y2="C" stroke="var(--ex-space-line)" />
        <line :x1="C" :y1="C - R" :x2="C" :y2="C + R" stroke="var(--ex-space-line)" />

        <circle :cx="C" :cy="C" r="38" fill="url(#ex-glow)" />
        <circle :cx="C" :cy="C" r="11" fill="url(#ex-earth)" />

        <line
          :x1="C"
          :y1="C"
          :x2="shown.x"
          :y2="shown.y"
          :stroke="shown.hazardous ? 'var(--ex-hazard)' : 'var(--ex-safe)'"
          stroke-width="1.5"
          stroke-dasharray="2 4"
        />
        <g
          v-for="d in dots"
          :key="d.name"
          class="cursor-pointer outline-none"
          tabindex="0"
          :aria-label="`${d.name}: ${d.distanceLd} distancias lunares, ${d.diameterM} metros`"
          @mouseenter="active = d.name"
          @focus="active = d.name"
          @click="active = d.name"
        >
          <circle :cx="d.x" :cy="d.y" :r="d.size + 8" fill="transparent" />
          <circle
            v-if="d.name === active"
            :cx="d.x"
            :cy="d.y"
            :r="d.size + 5"
            fill="none"
            :stroke="d.hazardous ? 'var(--ex-hazard)' : 'var(--ex-safe)'"
            stroke-width="1.5"
          />
          <circle :cx="d.x" :cy="d.y" :r="d.size" :fill="d.hazardous ? 'var(--ex-hazard)' : 'var(--ex-safe)'" />
        </g>
      </svg>

      <div class="flex flex-col justify-center gap-4 border-t border-[var(--ex-space-line)] p-6 md:border-t-0 md:border-l">
        <div class="flex flex-col gap-1">
          <span class="text-xs tracking-wide uppercase opacity-60">Asteroide</span>
          <span class="text-lg font-semibold text-white">{{ shown.name }}</span>
          <span
            class="w-fit rounded-full px-2 py-0.5 text-xs font-medium"
            :style="{
              color: shown.hazardous ? 'var(--ex-hazard)' : 'var(--ex-safe)',
              background: `color-mix(in oklab, ${shown.hazardous ? 'var(--ex-hazard)' : 'var(--ex-safe)'} 18%, transparent)`,
            }"
          >
            {{ shown.hazardous ? 'Potencialmente peligroso' : 'Sin riesgo' }}
          </span>
        </div>
        <dl class="grid grid-cols-2 gap-3 text-sm">
          <div>
            <dt class="opacity-60">Distancia</dt>
            <dd class="font-medium text-white">{{ shown.distanceLd }} DL</dd>
          </div>
          <div>
            <dt class="opacity-60">Diámetro</dt>
            <dd class="font-medium text-white">~{{ shown.diameterM }} m</dd>
          </div>
          <div class="col-span-2">
            <dt class="opacity-60">En kilómetros</dt>
            <dd class="font-medium text-white">{{ km(shown.distanceLd) }} km</dd>
          </div>
        </dl>
        <p class="text-xs opacity-60">Pasa el ratón por un punto. El tamaño del punto es el del asteroide.</p>
      </div>
    </div>
  </div>
</template>
