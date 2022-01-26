module.exports = {
  /*
  ** Headers of the page
  */
  head: {
    title: 'nov在线课堂 -java视频|前端视频|MySQL',
    meta: [
      { charset: 'utf-8' },
      { name: 'viewport', content: 'width=device-width, initial-scale=1' },
      { hid: 'keywords', name: 'keywords', content: 'java视频|前端视频|MySQL' },
      { hid: 'description', name: 'description', content: 'java视频|前端视频|MySQL' }
    ],
    link: [
      { rel: 'icon', type: 'image/x-icon', href: '/favicon.ico' }
    ]
  },
  /*
  ** Customize the progress bar color
  */
  loading: { color: '#3B8070' },
  /*
  ** Build configuration
  */
  build: {
    /*
    ** Run ESLint on save
    */
    extend (config, { isDev, isClient }) {
      if (isDev && isClient) {
        config.module.rules.push({
          enforce: 'pre',
          test: /\.(js|vue)$/,
          loader: 'eslint-loader',
          exclude: /(node_modules)/
        })
      }
    }
  },

  plugins: [
    { src: '~/plugins/nuxt-swiper-plugin.js', ssr: false },
    { src: '@/assets/icon/iconfont.js', ssr: false },
  ],

  css: [
    'swiper/dist/css/swiper.css',
    '@/assets/icon/iconfont.css'
  ],
}

