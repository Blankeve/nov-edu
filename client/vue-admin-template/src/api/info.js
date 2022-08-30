import request from '@/utils/request'



export function removeById(params) {
  return request({
    url: `/edu/info/remove/${params}`,
    method: 'delete',
  })
}


export function saveOrUpdate(data) {
  return request({
    url: '/edu/info/save',
    method: 'post',
    data
  })
}

export function getPage(params) {
  return request({
    url: '/edu/info/page',
    method: 'get',
    params
  })
}

export function exportPage(params) {
  return request({
    url: '/edu/info/export',
    method: 'get',
    responseType: 'blob',
    params
  })
}

export function getOneDetailByInfoId(params) {
  return request({
    url: `/edu/info/detail/${params}`,
    method: 'get',
  })
}