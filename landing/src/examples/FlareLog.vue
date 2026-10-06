<script setup lang="ts">
import { flares } from './data'

// A flare's class is its peak X-ray flux on a log scale: each letter is ten times the one before
// (B, C, M, X) and the number multiplies it. The bar runs from B1 to X10, four powers of ten.
const LETTERS = { A: -8, B: -7, C: -6, M: -5, X: -4 } as const
const flux = (classType: string) => {
  const letter = classType[0] as keyof typeof LETTERS
  return LETTERS[letter] + Math.log10(parseFloat(classType.slice(1)) || 1)
}
const width = (classType: string) => `${Math.min(100, Math.max(4, ((flux(classType) + 7) / 4) * 100))}%`
const colour = (classType: string) =>
  ({ X: 'var(--ex-flare-x)', M: 'var(--ex-flare-m)' })[classType[0] as 'X' | 'M'] ?? 'var(--ex-flare-c)'
const scale = ['B', 'C', 'M', 'X']
const strongest = flares.reduce((a, b) => (flux(a.classType) > flux(b.classType) ? a : b))
</script>

<template>
  <div class="flex flex-col gap-2" role="list" aria-label="Fulguraciones solares recientes, de la más nueva a la más antigua">
    <div class="grid grid-cols-[5.5rem_1fr] items-end gap-4 pb-1 text-xs text-fg-muted sm:grid-cols-[7rem_1fr_9rem]">
      <span />
      <div class="grid grid-cols-4">
        <span v-for="l in scale" :key="l" class="border-l border-border pl-1.5">{{ l }}</span>
      </div>
      <span class="hidden sm:block">Región</span>
    </div>

    <div
      v-for="f in flares"
      :key="f.date + f.peak"
      role="listitem"
      class="grid grid-cols-[5.5rem_1fr] items-center gap-4 rounded-[var(--radius-md)] py-2 sm:grid-cols-[7rem_1fr_9rem]"
    >
      <div class="flex flex-col leading-tight">
        <span class="font-medium text-fg">{{ f.date }}</span>
        <span class="text-xs text-fg-muted">{{ f.peak }} UTC</span>
      </div>
      <div class="relative h-8 rounded-full bg-bg-subtle">
        <div
          class="flex h-full items-center justify-end rounded-full pr-3 text-sm font-semibold text-white"
          :style="{
            width: width(f.classType),
            background: `linear-gradient(90deg, var(--ex-flare-c), ${colour(f.classType)})`,
            boxShadow: f === strongest ? `0 0 18px color-mix(in oklab, ${colour(f.classType)} 55%, transparent)` : undefined,
          }"
        >
          <span class="drop-shadow-[0_1px_1px_rgb(0_0_0/0.35)]">{{ f.classType }}</span>
        </div>
      </div>
      <span class="col-start-2 -mt-2 text-xs text-fg-muted sm:col-start-auto sm:mt-0">AR {{ f.region }}</span>
    </div>

    <p class="pt-2 text-sm text-fg-muted">
      Cada letra es diez veces más intensa que la anterior: la {{ strongest.classType }} del {{ strongest.date }} fue unas
      {{ Math.round(10 ** (flux(strongest.classType) - flux('C8.7'))) }} veces más fuerte que la C8.7.
    </p>
  </div>
</template>
