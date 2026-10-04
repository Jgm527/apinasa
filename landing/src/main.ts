import { createApp } from 'vue'
import { ElasticUi } from '@joseestevez/vue-elastic-ui'
import App from './App.vue'
import { router } from './router'
import './style.css'

createApp(App)
  .use(router)
  .use(ElasticUi, { labels: { note: 'Nota', tip: 'Consejo', copy: 'Copiar', copied: 'Copiado', mainNav: 'Secciones', previous: 'Anterior', next: 'Siguiente' } })
  .mount('#app')
