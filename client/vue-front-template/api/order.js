import request from '@/utils/request'

export function createOrder(data) {
    return request({
      url: '/order/trade/create',
      method: 'post',
      data
    })
  }

 
  export function getOrderById(params) {
    return request({
      url: `/order/trade/detail/${params}`,
      method: 'post',
    })
  }

  export function getOrderPage(params) {
    return request({
      url: '/order/trade/user',
      method: 'get',
      params
    })
  }

  export function getOrderByUidAndCourseId(params,uid) {
    return request({
      url: `/order/trade/hasbuy/${params}/${uid}/whi`,
      method: 'post',
    })
  }

 