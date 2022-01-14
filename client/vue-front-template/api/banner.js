import request from '@/utils/request'



export function getBannerList(data) {
    return request({
      url: '/edu/banner/client-list',
      method: 'get',
      data
    })
  }