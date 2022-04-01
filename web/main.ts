import ViteSSR from 'vite-ssr'
import routes from 'virtual:generated-pages'
import { getLocaleForUrl } from './modules/i18n'

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
  async (ctx) => {
  // Install Modules
  Object.values(import.meta.globEager('./modules/*.ts')).map((m) => m.install?.(ctx))
})
