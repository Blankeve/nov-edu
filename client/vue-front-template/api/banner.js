import request from '@/utils/request'



export function getBannerList(data) {
    return request({
      url: '/cms/banner/list',
      method: 'get',
      data
    })
  }