import { defineConfig, presetWind } from 'unocss';

export default defineConfig({
  presets: [presetWind()],
  theme: {
    fontFamily: {
      sans: ['Pretendard', 'system-ui', 'sans-serif'],
    },
  },
});
