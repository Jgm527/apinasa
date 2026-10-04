import { createApp } from 'vue'
import { ElasticUi } from '@joseestevez/vue-elastic-ui'
import App from './App.vue'
import './style.css'

createApp(App).use(ElasticUi, { labels: { note: 'Nota', copy: 'Copiar', copied: 'Copiado', mainNav: 'Secciones' } }).mount('#app')
