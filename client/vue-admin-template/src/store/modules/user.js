import { login, logout, getInfo } from '@/api/user'
import { getToken, setToken, removeToken } from '@/utils/auth'
import { resetRouter } from '@/router'
import Layout from '@/layout'
import { constantRoutes, theLastRoute } from '@/router'

const getDefaultState = () => {
  return {
    token: getToken(),
    username: '',
    roleName: '',
    avatar: '',
    routes: '',
    code: ''
  }
}

const state = getDefaultState()


export const importComponent = (view) => {
  return (resolve) => require([`@/views/${view}`], resolve)   //大坑
}

/**
 * 递归过滤异步路由表，返回符合用户角色权限的路由表
 * @param routes asyncRouterMap
 * @param roles
 */
function filterAsyncRouter(asyncRouterMap) {

  let accessedRouters = asyncRouterMap.filter(route => {
    if (route) {
      if (route.component) {
        if (route.component === 'Layout') {//Layout组件特殊处理
          route.component = Layout
        } else {
          route.component = importComponent(route.component)
        }
      }
      if (route.title) {
        route.meta = { title: route.title, icon: route.icon }
      }

      if (route.children && route.children.length) {
        route.children = filterAsyncRouter(route.children)
      }
      else
        delete route.children;   //大坑
    }
    delete route.id;
    delete route.parentId;
    return true
  })

  return accessedRouters
}


const mutations = {
  RESET_STATE: (state) => {
    Object.assign(state, getDefaultState())
  },
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  SET_NAME: (state, username) => {
    state.username = username
  },
  SET_AVATAR: (state, avatar) => {
    state.avatar = avatar
  },
  SET_CODE: (state, code) => {
    state.code = code
  },
  SET_ROLE: (state, roleName) => {
    state.roleName = roleName
  },
  SET_ROUTES: (state, routes) => {
    state.routes = constantRoutes.concat(routes).concat(theLastRoute)
  }
}

const actions = {
  // user login
  login({ commit }, userInfo) {
    const { username, password, code, uuid } = userInfo
    return new Promise((resolve, reject) => {
      login({ username: username.trim(), password: password, code: code, uuid: uuid }).then(response => {
        const { data } = response
        commit('SET_TOKEN', data.token)
        setToken(data.token)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // get user info
  getInfo({ commit, state }) {
    return new Promise((resolve, reject) => {
      getInfo(state.token).then(response => {
        const { data } = response
        console.log(data)
        if (!data) {
          removeToken();
        }

        const { username, avatar, code, roleName, menus } = data

        commit('SET_NAME', username)
        commit('SET_AVATAR', avatar)
        commit('SET_CODE', code)
        commit('SET_ROLE', roleName)
        let accessedRoutes = filterAsyncRouter(menus)
        commit('SET_ROUTES', accessedRoutes)

        resolve(data)
      }).catch(error => {
        removeToken();
      })
    })
  },

  // user logout
  logout({ commit, state }) {
    return new Promise((resolve, reject) => {
      removeToken() // must remove  token  first
      resetRouter()
      commit('RESET_STATE')
      resolve()
    })
  },

  // remove token
  resetToken({ commit }) {
    return new Promise(resolve => {
      removeToken() // must remove  token  first
      commit('RESET_STATE')
      resolve()
    })
  }

}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}

