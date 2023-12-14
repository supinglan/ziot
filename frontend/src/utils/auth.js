import Cookies from 'js-cookie'

const tokenKey = 'token'
const idKey = 'id'

export function getToken() {
  return Cookies.get(tokenKey)
}

export function setToken(token) {
  return Cookies.set(tokenKey, token)
}

export function removeToken() {
  return Cookies.remove(tokenKey)
}
export function getId() {
  return Cookies.get(idKey)
}

export function setId(id) {
  return Cookies.set(idKey, id)
}

export function removeId() {
  return Cookies.remove(idKey)
}
