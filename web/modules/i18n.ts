import { createI18n } from 'vue-i18n'

export const fallbackLocale = 'en'
export const messages = _getMessages()
export const supportLocales = Object.keys(messages)

export function getLocaleForUrl(url: string) {
  let locale = url.split('/')[1]

  if (!supportLocales.includes(locale) || !locale) {
    locale = fallbackLocale
  }

  return locale
}

export function install(ctx) {
  let locale = getLocaleForUrl(ctx.initialRoute.href) // URL 경로

  const i18n = createI18n({
    legacy: false,
    locale,
    fallbackLocale,
    messages,
  })

  ctx.app.use(i18n)
}

function _getMessages() {
  const messages = {}

  for (const [key, value] of Object.entries(import.meta.globEager('../locales/*.yml'))) {
    messages[key.replace(/^.*\/([^/]+)\.yml$/, '$1')] = value.default
  }

  return messages
}
