import { resolve } from 'path'
import { defineConfig } from 'vite'
import ssr from 'vite-ssr/plugin'
import vue from '@vitejs/plugin-vue'
import pages from 'vite-plugin-pages'
import i18n from '@intlify/vite-plugin-vue-i18n'
import unocss from 'unocss/vite'

export default defineConfig({
  plugins: [
    ssr(),
    vue(),
    pages({
      dirs: [resolve(__dirname, './web/pages')],
    }),
    unocss(),
    i18n({
      runtimeOnly: true,
      compositionOnly: true,
      include: resolve(__dirname, 'web/locales/**')
    })
  ],
  optimizeDeps: {
    include: ['vue', 'vue-router', '@vueuse/core'],
  }
})
