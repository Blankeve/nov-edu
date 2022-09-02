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
    url: '/edu/comment/page/whi',
    method: 'get',
    params
  })
}

export function reportComment(params) {
  return request({
    url: `/edu/comment/report/${params}`,
    method: 'put',
  })
}