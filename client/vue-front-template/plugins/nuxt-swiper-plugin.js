import Vue from 'vue'
import VueAwesomeSwiper from 'vue-awesome-swiper/dist/ssr'
import VueQriously from 'vue-qriously'
import ElementUi from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import VideoPlayer from 'vue-video-player'
require('video.js/dist/video-js.css')
require('vue-video-player/src/custom-theme.css')

Vue.use(VueAwesomeSwiper)
Vue.use(VueQriously)
Vue.use(ElementUi)
Vue.use(VideoPlayer)
