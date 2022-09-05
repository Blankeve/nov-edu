import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/ucenter/member/login-bg/whi',
    method: 'post',
    data
  })
}

export function getInfo() {
  return request({
    url: '/ucenter/member/info-bg/whi',
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

export function removeLoginHistoryById(params) {
  return request({
    url: `/ucenter/sys-login-history/remove/${params}`,
    method: 'delete',
  })
}

export function exportLoginHistoryPage(params) {
  return request({
    url: '/ucenter/sys-login-history/export',
    method: 'get',
    responseType: 'blob',
    params
  })
}

export function resetPwd(params) {
  return request({
    url: `/ucenter/member/reset-pwd/${params}`,
    method: 'put',
  })
}

export function exportPage(params) {
  return request({
    url: '/ucenter/member/export',
    method: 'get',
    responseType: 'blob',
    params
  })
}

export function getPicVerifyCode() {
  return request({
    url: '/ucenter/member/picVerifyCode/whi',
    method: 'get',
  })
}