import request from '@/utils/request'

export function getAWeekUserRegisterAndLoginCount(params) {
  return request({
    url: '/statistics/daily/register-and-access',
    method: 'get',
  })
}




