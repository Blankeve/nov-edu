import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/ucenter/member/login-bg',
    method: 'post',
    data
  })
}

export function getInfo(token) {
  return request({
    url: '/ucenter/member/info-bg',
    method: 'get',
    params: { token }
  })
}

export function logout() {
  return request({
    url: '/vue-admin-template/user/logout',
    method: 'post'
  })
}

export function getPage(params) {
  return request({
    url: '/ucenter/member/page',
    method: 'get',
    params
  })
}
