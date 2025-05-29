/**
 * plugins/vuetify.ts
 *
 * Framework documentation: https://vuetifyjs.com`
 */

// Styles
import '@mdi/font/css/materialdesignicons.css'
import 'vuetify/styles'

// Composables
import { createVuetify } from 'vuetify'
import { md3 } from 'vuetify/blueprints'
import { aliases, mdi } from 'vuetify/iconsets/mdi-svg' // 注意这里, 默认导入是 vuetify/iconsets/mdi 这样就错了

// https://vuetifyjs.com/en/introduction/why-vuetify/#feature-guides
export default createVuetify({
  theme: {
    defaultTheme: 'light',
  },
  blueprint: md3,
  icons: {
    defaultSet: 'mdi',
    aliases: {
      ...aliases,
    },
    sets: {
      mdi,
    },
  },
})
