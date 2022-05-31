import request from '@/utils/request'


export function getById(params) {
  return request({
    url: `/ucenter/member/info/${params}`,
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