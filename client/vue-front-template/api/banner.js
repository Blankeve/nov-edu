import request from '@/utils/request'



export function getBannerList(data) {
    return request({
      url: '/edu/banner/list/whi',
      method: 'get',
      data
    })
  }