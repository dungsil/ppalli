// (c) 2022 companyname All rights reserved.
import visualizer from 'rollup-plugin-visualizer'
import config from './vite.config'
import type { UserConfig } from 'vite'

(config as UserConfig).plugins.push(visualizer({
  open: true
}))

export default config
