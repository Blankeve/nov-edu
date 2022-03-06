import request from '@/utils/request'


  export function receiveNotice() {
    return request({
      url: `/edu/notice/receive`,
      method: 'get',
    })
  }

