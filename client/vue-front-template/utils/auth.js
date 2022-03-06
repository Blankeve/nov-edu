
import cookie from "js-cookie";

const TokenKey = 'access_token'
const infoKey = "login_info";

export function getToken() {
  return cookie.get(TokenKey)
}

export function setToken(token) {
  return cookie.set(TokenKey, token)
}

export function removeToken() {
  return cookie.set(TokenKey,"")
}

export function getInfo() {
  return cookie.get(infoKey)
}

export function setInfo(loginInfo) {
  return cookie.set(infoKey, loginInfo)
}

export function removeInfo() {
  return cookie.set(infoKey,"")
}