import request from '@/utils/request'

export function getList() {
  return request({
    url: '/edu/subject/list/whi',
    method: 'get',
  })
}

export function getParentList(params) {
  return request({
    url: `/edu/subject/list-parent/${params}`,
    method: 'get',
  })
}

export function update(data) {
  return request({
    url: '/edu/subject/update',
    method: 'put',
    data
  })
}