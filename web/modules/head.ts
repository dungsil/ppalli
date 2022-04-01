import { createHead } from '@vueuse/head'

export function install(ctx) {
  ctx.app.use(createHead())
}
