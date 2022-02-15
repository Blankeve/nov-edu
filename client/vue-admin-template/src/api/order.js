import request from '@/utils/request'


export function exportAll(params) {
  return request({
    url: '/order/trade/export-all',
    method: 'get',
    responseType: 'blob',
    params
  })
}

export function exportPage(params) {
  return request({
    url: '/order/trade/export',
    method: 'post',
    responseType: 'blob',
    params
  })
}

export function getOrderPage(params) {
  return request({
    url: '/order/trade/page',
    method: 'post',
    params
  })
}

export function removeOrderById(params) {
  return request({
    url: `/order/trade/remove/${params}`,
    method: 'delete',
  })
}