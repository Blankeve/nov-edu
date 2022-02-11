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

  export function getOrderByUidAndCourseId(params) {
    return request({
      url: `/order/trade/hasbuy/${params}`,
      method: 'post',
    })
  }

 