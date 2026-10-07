<script setup lang="ts">
import AsteroidRadar from './AsteroidRadar.vue'
import EarthSpin from './EarthSpin.vue'
import FlareLog from './FlareLog.vue'
import PatentSearch from './PatentSearch.vue'

// What a `nasa-example` fence in docs/apis.md turns into: something built from one API's response.
// The fence's text is what GitHub shows instead, so it is not used here.
defineProps<{ api: 'neows' | 'donki' | 'epic' | 'techtransfer'; code?: string }>()

const examples = { neows: AsteroidRadar, donki: FlareLog, epic: EarthSpin, techtransfer: PatentSearch }
</script>

<template>
  <div class="not-prose my-8">
    <component :is="examples[api]" />
  </div>
</template>
