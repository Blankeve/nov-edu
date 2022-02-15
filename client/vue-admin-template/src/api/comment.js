import request from '@/utils/request'

export function save(data) {
  return request({
    url: '/edu/comment/save',
    method: 'post',
    data
  })
}

export function exportAll(params) {
  return request({
    url: '/edu/comment/export-all',
    method: 'get',
    responseType: 'blob',
    params
  })
}

export function exportPage(params) {
  return request({
    url: '/edu/comment/export',
    method: 'post',
    responseType: 'blob',
    params
  })
}

export function getCommentPage(params) {
  return request({
    url: '/edu/comment/page',
    method: 'post',
    params
  })
}

export function removeCommentById(params) {
  return request({
    url: `/edu/comment/remove/${params}`,
    method: 'delete',
  })
}