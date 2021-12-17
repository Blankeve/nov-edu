import request from '@/utils/request'

export function getList() {
  return request({
    url: '/edu/edu-subject/list',
    method: 'get',
  })
}