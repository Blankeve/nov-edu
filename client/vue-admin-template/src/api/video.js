import request from '@/utils/request'

export function saveVideo(data) {
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

  export function getOneByVideoId(params) {
    return request({
      url: `/edu/video/detail/${params}`,
      method: 'post',
    })
  }

  export function removeVideoById(params) {
    return request({
      url: `/edu/video/remove/${params}`,
      method: 'delete',
    })
  }