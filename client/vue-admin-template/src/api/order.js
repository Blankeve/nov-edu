import request from '@/utils/request'



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