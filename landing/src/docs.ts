import type { Component } from 'vue'
import { BookOpen, Boxes, Download, Globe, Rocket, Send, Telescope } from '@lucide/vue'

// The docs are the repository's Markdown, read as they are written in docs/.
const files = import.meta.glob('../../docs/*.md', { query: '?raw', import: 'default', eager: true }) as Record<string, string>

const REPO_BLOB = 'https://github.com/Jgm527/apinasa/blob/main'

export interface DocPage {
  slug: string
  title: string
  /** One line on what the page covers, shown on the next-page card. */
  description: string
  icon: Component
  source: string
}

// Order and names in the sidebar. `index` is docs/README.md.
const ORDER: { file: string; slug: string; title: string; description: string; icon: Component }[] = [
  { file: 'README', slug: 'index', title: 'Introducción', description: 'Qué hay en el proyecto y por dónde empezar.', icon: BookOpen },
  { file: 'primera-peticion', slug: 'primera-peticion', title: 'Tu primera petición', description: 'Una petición real a la API, de principio a fin.', icon: Rocket },
  { file: 'postman', slug: 'postman', title: 'Probar con Postman', description: 'Explora las APIs de NASA con peticiones HTTP visuales.', icon: Send },
  { file: 'instalacion', slug: 'instalacion', title: 'Instalación', description: 'Requisitos, clave de NASA y cómo arrancar.', icon: Download },
  { file: 'apis', slug: 'apis', title: 'Las APIs de NASA', description: 'Qué devuelve cada servicio, con ejemplos.', icon: Telescope },
  { file: 'ejemplos', slug: 'ejemplos', title: 'Ejemplos mínimos', description: 'El @Controller y el @RestController, reducidos al mínimo.', icon: Boxes },
  { file: 'conceptos-spring', slug: 'conceptos-spring', title: 'Conceptos de Spring Boot', description: 'Los términos de Spring Boot que salen en el código.', icon: Globe },
]

const slugOf = new Map(ORDER.map((d) => [d.file, d.slug]))

// Links between the docs become routes of the site; links out of docs/ go to the repository.
function relink(source: string): string {
  return source.replace(/\]\(([^)\s]+)\)/g, (match, href: string) => {
    if (/^(https?:|#|\/)/.test(href)) return match
    const [path, hash = ''] = href.split('#')
    const doc = path.match(/^(?:\.\/)?([\w-]+)\.md$/)
    const slug = doc && slugOf.get(doc[1])
    if (slug) return `](/docs/${slug}${hash ? '#' + hash : ''})`
    const fromRoot = path.replace(/^(\.\.\/)+/, '')
    return `](${REPO_BLOB}/${path.startsWith('../') ? fromRoot : 'docs/' + fromRoot})`
  })
}

export const docPages: DocPage[] = ORDER.map(({ file, slug, title, description, icon }) => ({
  slug,
  title,
  description,
  icon,
  source: relink(files[`../../docs/${file}.md`] ?? `# ${title}\n\nFalta docs/${file}.md.`),
}))
