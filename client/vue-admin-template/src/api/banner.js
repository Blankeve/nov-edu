import request from '@/utils/request'

export function getList(params) {
  return request({
    url: '/edu/banner/list',
    method: 'get',
    params
  })
}


export function removeById(params) {
  return request({
    url: `/edu/banner/remove/${params}`,
    method: 'delete',
  })
}


export function saveOrUpdate(params) {
  return request({
    url: '/edu/banner/save',
    method: 'post',
    params
  })
}

