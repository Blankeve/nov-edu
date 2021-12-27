import request from '@/utils/request'

export function save(data) {
    return request({
      url: '/edu/video/save',
      method: 'post',
      data
    })
  }

  export function getPage(params) {
    return request({
      url: '/edu/video/page',
      method: 'post',
      params
    })
  }
