import { loginMember, getInfo } from '@/api/user'
import { getToken, setToken, removeToken } from '@/utils/auth'

const getDefaultState = () => {
  return {
    token: getToken(),
    name: '',
    nickname: undefined,
    avatar: '',
    uid: '',
    routes: '',
    code: ''
  }
}

const state = getDefaultState()





const mutations = {
  RESET_STATE: (state) => {
    Object.assign(state, getDefaultState())
  },
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  SET_UID: (state, uid) => {
    state.uid = uid
  },
  SET_NICKNAME: (state, nickname) => {
    state.nickname = nickname
  },
  SET_AVATAR: (state, avatar) => {
    state.avatar = avatar
  },
  SET_CODE: (state, code) => {
    state.code = code
  },
  SET_ROLE: (state, role) => {
    state.role = role
  },
}

const actions = {
  // user login
  login({ commit }, userInfo) {
    const { username, password } = userInfo
    return new Promise((resolve, reject) => {
      loginMember({ username: username.trim(), password: password }).then(response => {
        const { data } = response
        setToken(data.access_token)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // get user info
  getInfo({ commit, state }) {
    return new Promise((resolve, reject) => {

      getInfo(getToken()).then(response => {
        const { data } = response

        if (!data) {
          removeToken();
          return reject('登录失效，请重新登录')
        }

        const { uid, avatar, nickname } = data

        commit('SET_NICKNAME', nickname)
        commit('SET_AVATAR', avatar)
        commit('SET_UID', uid)
        resolve(data)
      }).catch(error => {
        removeToken();
        return reject('登录失效，请重新登录')
      })
    })
  },

  // user logout
  logout({ commit, state }) {
    return new Promise((resolve, reject) => {
      removeToken() // must remove  token  first
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

