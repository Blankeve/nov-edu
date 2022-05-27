import request from '@/utils/request'

export function getPage(params) {
  return request({
    url: '/edu/consult/page',
    method: 'post',
    params
  })
}

export function save(params) {
  return request({
    url: '/edu/consult/save',
    method: 'post',
    params
  })
}

export function update(params) {
  return request({
    url: '/edu/consult/update',
    method: 'put',
    params
  })
}

export function removeById(params) {
  return request({
    url: `/edu/consult/remove/${params}`,
    method: 'delete',
  })
}