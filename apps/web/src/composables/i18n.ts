// (c) 2022 companyname All rights reserved.
import type { useI18n } from 'vue-i18n'

type I18n = ReturnType<typeof useI18n>

/**
 * 해당 페이지의 이름을 생성
 * @param t 번역
 * @param pageName 페이지 이름
 */
export function createTitle(t: Function, pageName: string) {
  return `${t(pageName + '.title')} · ${t('global.title')}`
}
