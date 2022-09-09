import request from '@/utils/request'



export function removeById(params) {
  return request({
    url: `/edu/notice/remove/${params}`,
    method: 'delete',
  })
}


export function saveOrUpdate(data) {
  return request({
    url: '/edu/notice/save',
    method: 'post',
    data
  })
}

export function getPage(params) {
  return request({
    url: '/edu/notice/page',
    method: 'get',
    params
  })
}