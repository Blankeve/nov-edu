import request from '@/utils/request'



export function exportPage(params) {
  return request({
    url: '/order/trade/export',
    method: 'get',
    responseType: 'blob',
    params
  })
}

export function getOrderPage(params) {
  return request({
    url: '/order/trade/page',
    method: 'get',
    params
  })
}

export function removeOrderById(params) {
  return request({
    url: `/order/trade/remove/${params}`,
    method: 'delete',
  })
}