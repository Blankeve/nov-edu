import request from '@/utils/request'




export function getPage(params) {
  return request({
    url: '/edu/info/page',
    method: 'post',
    params
  })
}

export function getOneDetailByInfoId(params) {
  return request({
    url: `/edu/info/detail-client/${params}`,
    method: 'get',
  })
}