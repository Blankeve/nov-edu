import request from '@/utils/request'

export function getPage(params) {
  return request({
    url: '/edu/oper-log/page',
    method: 'get',
    params
  })
}

