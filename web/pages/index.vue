<script lang="ts" setup>

import { useI18n } from 'vue-i18n'
import { useHead } from '@vueuse/head'

const { t } = useI18n()

useHead({
  title: t('index.title')
})
</script>

<template>
  <div class="font-sans">{{ t('index.hello') }} {{ t('index.world') }}</div>
</template>
