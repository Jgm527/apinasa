import { createRouter, createWebHistory } from 'vue-router'
import DocsPage from './pages/DocsPage.vue'
import LandingPage from './pages/LandingPage.vue'

// Paths, as the library's TableOfContents and Markdown links expect. A static host must answer
// every path with index.html.
export const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', component: LandingPage },
    { path: '/docs/:slug?', component: DocsPage },
  ],
  // An id may start with a digit, which is no valid CSS selector: look it up by id.
  scrollBehavior: (to) => {
    const el = to.hash ? document.getElementById(decodeURIComponent(to.hash.slice(1))) : null
    return el ? { el, top: 80 } : { top: 0 }
  },
})
