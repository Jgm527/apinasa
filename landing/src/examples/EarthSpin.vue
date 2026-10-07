<script setup lang="ts">
import { computed, onBeforeUnmount, ref } from 'vue'
import { Slider } from '@joseestevez/vue-elastic-ui'
import { Pause, Play } from '@lucide/vue'
import { cities, continents, epicDay } from './data'

// EPIC looks at the Earth from the Sun's side, so the centre of every photo is close to where it
// is noon. That point moves 15° west each hour: here the globe is drawn as the camera saw it.
const SIZE = 320
const C = SIZE / 2
const R = 140
const LAT0 = (-4 * Math.PI) / 180 // early October: the camera sees a little more of the south

const hour = ref(epicDay[7]!.time)
const lon0 = computed(() => {
  const l = 182.8 - 15 * hour.value
  return ((((l + 180) % 360) + 360) % 360) - 180
})

const rad = (d: number) => (d * Math.PI) / 180
/** Orthographic projection: [x, y, facing us]. */
function project([lon, lat]: [number, number]): [number, number, boolean] {
  const φ = rad(lat)
  const dλ = rad(lon - lon0.value)
  const x = Math.cos(φ) * Math.sin(dλ)
  const y = Math.cos(LAT0) * Math.sin(φ) - Math.sin(LAT0) * Math.cos(φ) * Math.cos(dλ)
  const front = Math.sin(LAT0) * Math.sin(φ) + Math.cos(LAT0) * Math.cos(φ) * Math.cos(dλ) > 0
  return [C + R * x, C - R * y, front]
}

const range = (from: number, to: number, step: number) =>
  Array.from({ length: Math.floor((to - from) / step) + 1 }, (_, i) => from + i * step)

/** A line through points, broken where it goes behind the globe. */
function line(points: [number, number][]) {
  let d = ''
  let pen = false
  for (const p of points) {
    const [x, y, front] = project(p)
    if (!front) {
      pen = false
      continue
    }
    d += `${pen ? 'L' : 'M'}${x.toFixed(1)},${y.toFixed(1)}`
    pen = true
  }
  return d
}

/** A filled outline; points behind the globe are pushed onto its edge. */
function shape(points: [number, number][]) {
  // Each edge in short steps, so the part pushed onto the edge follows the globe's curve.
  const dense = points.flatMap((a, i) => {
    const b = points[(i + 1) % points.length]!
    const steps = Math.ceil(Math.max(Math.abs(b[0] - a[0]), Math.abs(b[1] - a[1])) / 4) || 1
    return range(0, steps - 1, 1).map((k): [number, number] => [a[0] + ((b[0] - a[0]) * k) / steps, a[1] + ((b[1] - a[1]) * k) / steps])
  })
  const projected = dense.map(project)
  if (!projected.some((p) => p[2])) return ''
  return (
    projected
      .map(([x, y, front], i) => {
        if (!front) {
          const k = R / Math.hypot(x - C, y - C)
          ;[x, y] = [C + (x - C) * k, C + (y - C) * k]
        }
        return `${i ? 'L' : 'M'}${x.toFixed(1)},${y.toFixed(1)}`
      })
      .join('') + 'Z'
  )
}

const meridians = computed(() => range(-180, 150, 30).map((lon) => line(range(-90, 90, 5).map((lat) => [lon, lat]))))
const parallels = computed(() => [-60, -30, 0, 30, 60].map((lat) => line(range(-180, 180, 5).map((lon) => [lon, lat]))))
const land = computed(() => continents.map(shape))
const places = computed(() =>
  cities
    .map((c) => {
      const [x, y, front] = project(c.at)
      return { ...c, x, y, front, near: Math.hypot(x - C, y - C) < R * 0.8 }
    })
    .filter((c) => c.front),
)

const region = computed(() => {
  const l = lon0.value
  if (l > 120 || l < -150) return 'el Pacífico'
  if (l > 60) return 'Asia'
  if (l > -20) return 'Europa y África'
  if (l > -80) return 'el Atlántico y Sudamérica'
  return 'Norteamérica'
})
const clock = (h: number) =>
  `${String(Math.floor(h) % 24).padStart(2, '0')}:${String(Math.round((h % 1) * 60) % 60).padStart(2, '0')}`
const degrees = (d: number) => `${Math.abs(Math.round(d))}° ${d < 0 ? 'O' : 'E'}`

let frame = 0
const playing = ref(false)
function toggle() {
  playing.value = !playing.value
  if (!playing.value) return cancelAnimationFrame(frame)
  let last = performance.now()
  const tick = (now: number) => {
    hour.value = (hour.value + ((now - last) / 1000) * 3) % 24 // three hours a second
    last = now
    frame = requestAnimationFrame(tick)
  }
  frame = requestAnimationFrame(tick)
}
onBeforeUnmount(() => cancelAnimationFrame(frame))
</script>

<template>
  <div class="grid overflow-hidden rounded-[var(--radius-lg)] bg-[var(--ex-space)] text-[var(--ex-space-text)] md:grid-cols-[1fr_16rem]">
    <svg
      :viewBox="`0 0 ${SIZE} ${SIZE}`"
      class="mx-auto w-full max-w-sm p-4"
      role="img"
      :aria-label="`La Tierra vista por EPIC a las ${clock(hour)} UTC, con ${region} en el centro`"
    >
      <defs>
        <radialGradient id="ex-ocean" cx="42%" cy="38%" r="70%">
          <stop offset="0%" stop-color="#3f7fe0" />
          <stop offset="70%" stop-color="var(--ex-ocean)" />
          <stop offset="100%" stop-color="var(--ex-ocean-deep)" />
        </radialGradient>
        <radialGradient id="ex-atmosphere">
          <stop offset="88%" stop-color="#7fb8ff" stop-opacity="0.35" />
          <stop offset="100%" stop-color="#7fb8ff" stop-opacity="0" />
        </radialGradient>
        <clipPath id="ex-disc"><circle :cx="C" :cy="C" :r="R" /></clipPath>
      </defs>
      <circle :cx="C" :cy="C" :r="R + 12" fill="url(#ex-atmosphere)" />
      <circle :cx="C" :cy="C" :r="R" fill="url(#ex-ocean)" />
      <g clip-path="url(#ex-disc)">
        <path v-for="(d, i) in land" :key="i" :d="d" fill="var(--ex-land)" fill-opacity="0.9" />
        <path v-for="(d, i) in meridians" :key="'m' + i" :d="d" fill="none" stroke="white" stroke-opacity="0.18" />
        <path v-for="(d, i) in parallels" :key="'p' + i" :d="d" fill="none" stroke="white" stroke-opacity="0.18" />
      </g>
      <g v-for="p in places" :key="p.name">
        <circle :cx="p.x" :cy="p.y" r="2.5" fill="white" />
        <text v-if="p.near" :x="p.x + 5" :y="p.y + 4" font-size="11" fill="white" style="paint-order: stroke" stroke="#08122b" stroke-width="3">
          {{ p.name }}
        </text>
      </g>
      <circle :cx="C" :cy="C" r="4" fill="none" stroke="var(--ex-hazard)" stroke-width="1.5" />
    </svg>

    <div class="flex flex-col justify-center gap-5 border-t border-[var(--ex-space-line)] p-6 md:border-t-0 md:border-l">
      <div class="flex flex-col gap-1">
        <span class="text-xs tracking-wide uppercase opacity-60">Foto de las</span>
        <span class="font-mono text-3xl font-semibold text-white tabular-nums">{{ clock(hour) }} <span class="text-base opacity-60">UTC</span></span>
        <span class="text-sm">Centro: {{ degrees(lon0) }}, sobre {{ region }}</span>
      </div>
      <div class="flex items-center gap-3">
        <button
          type="button"
          class="grid size-10 shrink-0 place-items-center rounded-full bg-white text-[var(--ex-space)] transition-transform focus-ring hover:scale-105"
          :aria-label="playing ? 'Pausar' : 'Ver girar la Tierra'"
          @click="toggle"
        >
          <component :is="playing ? Pause : Play" class="size-4" fill="currentColor" aria-hidden="true" />
        </button>
        <Slider v-model="hour" :min="0" :max="23.75" :step="0.25" :format="clock" label="Hora de la foto" class="flex-1" />
      </div>
      <div class="flex flex-col gap-2">
        <span class="text-xs opacity-60">Fotos de ese día</span>
        <div class="flex flex-wrap gap-1.5">
          <button
            v-for="p in epicDay"
            :key="p.time"
            type="button"
            class="rounded-full px-2 py-0.5 font-mono text-xs transition-colors focus-ring"
            :class="Math.abs(p.time - hour) < 0.01 ? 'bg-white text-[var(--ex-space)]' : 'bg-white/10 hover:bg-white/20'"
            @click="hour = p.time"
          >
            {{ clock(p.time) }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>
