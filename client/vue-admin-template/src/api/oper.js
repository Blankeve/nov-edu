import request from '@/utils/request'

export function getPage(params) {
  return request({
    url: '/ucenter/oper-log/page',
    method: 'get',
    params
  })
}

export function removeById(params) {
  return request({
    url: `/ucenter/oper-log/remove/${params}`,
    method: 'delete',
  })
}

export function exportPage(params) {
  return request({
    url: '/ucenter/oper-log/export',
    method: 'get',
    responseType: 'blob',
    params
  })
}