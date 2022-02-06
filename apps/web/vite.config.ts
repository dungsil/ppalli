// (c) 2022 companyname All rights reserved.
import { resolve } from 'path'
import { defineConfig } from 'vite'
import Vue from '@vitejs/plugin-vue'
import Pages from 'vite-plugin-pages'
import Layouts from 'vite-plugin-vue-layouts'
import I18n from '@intlify/vite-plugin-vue-i18n'
import WindiCSS from 'vite-plugin-windicss'

export default defineConfig({
  plugins: [Vue(), Pages(), Layouts(), I18n({ include: resolve(__dirname, 'locales/**') }), WindiCSS()],
})
