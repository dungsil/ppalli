import { createI18n } from 'vue-i18n'
import messages from '@intlify/vite-plugin-vue-i18n/messages'
import type { ViteModule } from '../../types/vite-ssg'

export const install: ViteModule = ({ app }) => {
  const $i18n = createI18n({
    locale: 'ko-KR',
    messages,
  })

  app.use($i18n)
}
