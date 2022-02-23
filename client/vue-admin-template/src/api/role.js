import request from '@/utils/request'


export function saveOrUpdate(data) {
  return request({
    url: '/ucenter/role/save',
    method: 'post',
    data
  })
}

export function exportAll(params) {
  return request({
    url: '/ucenter/role/export-all',
    method: 'get',
    responseType: 'blob',
    params
  })
}

export function exportPage(params) {
  return request({
    url: '/ucenter/role/export',
    method: 'post',
    responseType: 'blob',
    params
  })
}

export function getList() {
  return request({
    url: '/ucenter/role/list',
    method: 'get',
  })
}

export function getPage(params) {
  return request({
    url: '/ucenter/role/page',
    method: 'post',
    params
  })
}

export function removeById(params) {
  return request({
    url: `/ucenter/role/remove/${params}`,
    method: 'delete',
  })
}

export function saveRoleByUid(data) {
  return request({
    url: '/ucenter/role/assign-role-uid',
    method: 'post',
    data
  })
}