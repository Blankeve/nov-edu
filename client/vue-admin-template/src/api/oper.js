import request from '@/utils/request'

export function getPage(params) {
  return request({
    url: '/ucenter/oper-log/page',
    method: 'get',
    params
  })
}

