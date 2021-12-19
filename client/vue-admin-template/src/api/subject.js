import request from '@/utils/request'

export function getList() {
  return request({
    url: '/edu/edu-subject/list',
    method: 'get',
  })
}

export function update(data) {
  return request({
    url: '/edu/edu-subject/update',
    method: 'put',
    data
  })
}