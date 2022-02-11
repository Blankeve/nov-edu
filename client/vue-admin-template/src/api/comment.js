import request from '@/utils/request'

export function save(data) {
  return request({
    url: '/edu/comment/save',
    method: 'post',
    data
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