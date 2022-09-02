import request from '@/utils/request'

export function getPage(params) {
  return request({
    url: '/edu/consult/page/whi',
    method: 'get',
    params
  })
}

export function save(params) {
  return request({
    url: '/edu/consult/save',
    method: 'post',
    params
  })
}

