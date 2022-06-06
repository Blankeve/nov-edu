import request from '@/utils/request'



  export function getPage(params) {
    return request({
      url: '/edu/video/page',
      method: 'post',
      params
    })
  }

  export function getHistoryWatchPage(params) {
    return request({
      url: '/edu/video/history-watch/page',
      method: 'post',
      params
    })
  }

  export function getOneByVideoId(params) {
    return request({
      url: `/edu/video/detail-client/${params}`,
      method: 'post',
    })
  }

  