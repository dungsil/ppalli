<script lang="ts" setup>
import { useI18n } from 'vue-i18n'
import { useHead } from '@vueuse/head'

const { t, locale } = useI18n()

useHead({
  htmlAttrs: { lang: locale }, // 브라우저에게 해당 페이지의 언어를 알림
  title: t('meta.title') // 기본 타이틀 값 지정
})
</script>

<template>
  <router-view />
</template>
