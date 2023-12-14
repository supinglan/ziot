import { login, register, getInfo } from '@/api/user'
import { getToken, setToken, removeToken, getId, setId } from '@/utils/auth'
import { resetRouter } from '@/router'
import { Message } from 'element-ui'
const getDefaultState = () => {
  return {
    token: getToken(),
    name: '',
    id: getId()
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
  SET_NAME: (state, name) => {
    state.name = name
  },
  SET_ID: (state, id) => {
    state.id = id
  }
}

const actions = {
  login(context, userInfo) {
    const { username, password } = userInfo
    return new Promise((resolve, reject) => {
      login({ username: username.trim(), password: password }).then(res => {
        context.commit('SET_TOKEN', res.token)
        setToken(res.token)
        context.commit('SET_ID', res.id)
        setId(res.id)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  register(context, userInfo) {
    const { username, email, password } = userInfo
    return new Promise((resolve, reject) => {
      register({ username: username.trim(), email: email.trim(), password: password }).then(response => {
        if (!('message' in response)) {
          Message({
            message: '注册成功',
            type: 'success',
            duration: 5 * 1000
          })
        }
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // get user info
  getInfo({ commit, state }) {
    return new Promise((resolve, reject) => {
      getInfo(state.id).then(res => {
        if (!res) {
          return reject('Verification failed, please Login again.')
        }
        const name = res.username
        commit('SET_NAME', name)
        resolve(res)
      }).catch(error => {
        reject(error)
      })
    })
  },

  // user logout
  logout({ commit, state }) {
    removeToken() // must remove  token  first
    resetRouter()
    commit('RESET_STATE')
  },

  // remove token
  resetToken({ commit }) {
    return new Promise(resolve => {
      removeToken() // must remove  token  first
      commit('RESET_STATE')
      resolve()
    })
  },
  switch({ commit }, userInfo) {
    const { username, password } = userInfo
    return new Promise((resolve, reject) => {
      login({ username: username.trim(), password: password }).then(response => {
        const { data } = response
        commit('SET_TOKEN', data.token)
        setToken(data.token)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}

