
const TokenKey = 'access_token'

export function getToken() {
  if (process.client)
    return localStorage.getItem(TokenKey)
}

export function setToken(token) {
  return localStorage.setItem(TokenKey, token)
}

export function removeToken() {
  if (process.client)
    return localStorage.removeItem(TokenKey)
}
