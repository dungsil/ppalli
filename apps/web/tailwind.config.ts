// (c) 2022 companyname All rights reserved.
import { defineConfig } from 'windicss/helpers'

export default defineConfig({
  theme: {
    extend: {
      fontFamily: {
        sans: ['Pretendard Variable', 'sans-serif'],
      },
      fontSize: {
        base: '16px',
      },

      colors: {
        gray: {
          0: '#f8f9fa',
          1: '#f1f3f5',
          2: '#e9ecef',
          3: '#dee2e6',
          4: '#ced4da',
          5: '#adb5bd',
          6: '#868e96',
          7: '#495057',
          8: '#343a40',
          9: '#212529',
        },
      },
    },
  },
})
