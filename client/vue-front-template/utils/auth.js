
import cookie from "js-cookie";

const TokenKey = 'access_token'

export function getToken() {
  return cookie.get(TokenKey)
}

export function setToken(token) {
  return cookie.set(TokenKey, token)
}

export function removeToken() {
  return cookie.set(TokenKey,"")
}
