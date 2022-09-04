import request from '@/utils/request'




export function getPage(params) {
  return request({
    url: '/edu/info/page/whi',
    method: 'get',
    params
  })
}

export function getOneDetailByInfoId(params) {
  return request({
    url: `/edu/info/detail/${params}/whi`,
    method: 'get',
  })
}