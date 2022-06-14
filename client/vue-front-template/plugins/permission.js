
import { getToken } from '@/utils/auth'
import { Message } from 'element-ui'
import { mapGetters } from 'vuex'
export default ({ app, store }) => {


    app.router.beforeEach(async (to, from, next) => {
        if (process.client) {
            const hasToken = getToken()
            if (hasToken) {
                if (to.path === '/login') {
                } else {
                    const hasGetUserInfo = store.getters.nickname
                    if (hasGetUserInfo) {
                        next()
                    } else {
                        try {
                            await store.dispatch('user/getInfo').then(resp => {
                                next()
                            })
                        } catch (error) {
                            // remove token and go to login page to re-login
                            // await store.dispatch('user/resetToken')
                            Message.error(error || '未知错误')
                            console.log(error)
                        }
                    }
                }
            } else {
                await store.dispatch("user/logout").then(() => {
                }).catch(() => {
                });
                next();
            }
        }
    });
};


