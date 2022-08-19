import request from '@/utils/request'


  export function receiveNotice(params) {
    return request({
      url: `/edu/notice/receive`,
      method: 'get',
      params
    })
  }

