import request from '@/utils/request'

export function getList(params) {
    return request({
      url: '/edu/edu-teacher/list',
      method: 'get',
      params
    })
  }