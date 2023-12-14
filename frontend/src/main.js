import Vue from 'vue'

import 'normalize.css/normalize.css' // A modern alternative to CSS resets

import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'

import '@/styles/index.scss' // global css

import App from './App'
import store from './store'
import router from './router'
import axios from 'axios'

import '@/icons' // icon
import '@/permission' // permission control
import echarts from 'echarts'
import VueAMap from 'vue-amap'
import '@vuemap/vue-amap/dist/style.css'
import 'lib-flexible/flexible.js'

Vue.use(ElementUI)

Vue.use(VueAMap)
VueAMap.initAMapApiLoader({
  key: 'f016cec89837e26da5a55ad23a49bc63',
  Plugins: [
    'Scale', 'ToolBar', 'MapType', 'OverView', 'ControlBar', 'PolyEditor'
  ]
})
axios.defaults.withCredentials = true
Vue.prototype.$echarts = echarts
Vue.config.productionTip = false
Vue.config.silent = true

new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})
