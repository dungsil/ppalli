import ViteSSR from 'vite-ssr'
import routes from 'virtual:generated-pages'
import { getLocaleForUrl } from './modules/i18n'

import '@unocss/reset/sanitize/sanitize.css'
import '@unocss/reset/sanitize/forms.css'
import '@unocss/reset/sanitize/assets.css'
import 'uno.css'

import App from './App.vue'

export default ViteSSR(
  App,
  {
    routes,
    base: ({ url }) => {
      return `/${getLocaleForUrl(url.pathname)}/`
    }
  },
  (ctx) => {
    Object.values(import.meta.globEager('./modules/*.ts')).map((m) => m.install?.(ctx))
})
