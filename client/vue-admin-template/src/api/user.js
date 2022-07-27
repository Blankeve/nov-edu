import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/ucenter/member/login-bg',
    method: 'post',
    data
  })
}

export function getInfo() {
  return request({
    url: '/ucenter/member/info-bg',
    method: 'get',
  })
}

export function getUserLoginInfo() {
  return request({
    url: '/ucenter/member/login-info',
    method: 'get',
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

export function getLoginHistoryPage(params) {
  return request({
    url: '/ucenter/sys-login-history/page',
    method: 'get',
    params
  })
}

export function resetPwd(params) {
  return request({
    url: `/ucenter/member/reset-pwd/${params}`,
    method: 'put',
  })
}

export function exportAll() {
  return request({
    url: '/ucenter/member/export-all',
    method: 'get',
    responseType: 'blob'
  })
}

export function exportPage(params) {
  return request({
    url: '/ucenter/member/export',
    method: 'post',
    responseType: 'blob',
    params
  })
}

export function getPicVerifyCode() {
  return request({
    url: '/ucenter/member/picVerifyCode',
    method: 'get',
  })
}