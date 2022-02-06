<!-- (c) 2022 companyname All rights reserved. -->
<script lang="ts" setup>
import { useHead } from '@vueuse/head'
import { useI18n } from 'vue-i18n'

const { t, locale } = useI18n()

useHead({
  htmlAttrs: [
    { lang: locale }
  ],
  title: t('global.title')
})
</script>

<template>
  <div class="box-border font-sans font-normal text-base text-gray-8 tracking-normal leading-normal">
    <router-view/>
  </div>
</template>
