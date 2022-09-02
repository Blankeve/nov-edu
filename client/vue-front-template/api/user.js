import request from '@/utils/request'

export function loginMember(params) {
  return request({
    url: '/ucenter/member/login/whi',
    method: 'post',
    params
  })
}

export function getById(params) {
  return request({
    url: `/ucenter/member/info/${params}/whi`,
    method: 'post',
  })
}

export function updatePwdById(data) {
  return request({
    url: '/ucenter/member/pwd',
    method: 'put',
    data
  })
}

export function updateById(data) {
  return request({
    url: '/ucenter/member/profile',
    method: 'put',
    data
  })
}

export function getInfo(token) {
  return request({
    url: '/ucenter/member/info-client',
    method: 'get',
    params: { token }
  })
}