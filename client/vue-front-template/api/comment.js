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
    url: '/edu/comment/page-client',
    method: 'post',
    params
  })
}