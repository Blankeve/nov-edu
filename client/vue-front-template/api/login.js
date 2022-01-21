import request from '@/utils/request'



export function loginMember(params) {
    return request({
      url: '/ucenter/member/login',
      method: 'post',
      params
    })
  }