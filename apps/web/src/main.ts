// (c) 2022 companyname All rights reserved.
import type { ViteSSGContext } from 'vite-ssg'
import { ViteSSG } from 'vite-ssg'
import generatedPages from 'virtual:generated-pages'
import { setupLayouts } from 'virtual:generated-layouts'
import App from './App.vue'

// WindiCSS
import 'virtual:windi-base.css'
import 'virtual:windi-components.css'
import 'virtual:windi-utilities.css'
import 'virtual:windi-devtools'

// noinspection JSUnusedGlobalSymbols
export const createApp = ViteSSG(
  App,
  {
    base: import.meta.env.BASE_URL,
    routes: setupLayouts(generatedPages),
  },
  loadModules
)

function loadModules(ctx: ViteSSGContext) {
  const modules = Object.values(import.meta.globEager('./modules/**/*.ts'))
  for (const module of modules) {
    module.install?.(ctx)
  }
}
