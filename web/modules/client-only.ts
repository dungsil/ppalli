import  { ClientOnly } from 'vite-ssr'

export function install (ctx) {
  ctx.app.component(ClientOnly.name, ClientOnly)
}
