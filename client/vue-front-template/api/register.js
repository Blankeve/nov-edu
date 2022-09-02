import request from '@/utils/request'



export function registerMember(params) {
    return request({
      url: '/ucenter/member/register/whi',
      method: 'post',
      params
    })
  }