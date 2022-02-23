import request from '@/utils/request'

export function saveOrUpdate(data) {
  return request({
    url: '/ucenter/permission/save',
    method: 'post',
    data
  })
}

export function getTree() {
  return request({
    url: '/ucenter/permission/tree',
    method: 'get',
  })
}

export function queryMenuByRoleId(params) {
  return request({
    url: `/ucenter/permission/role/${params}`,
    method: 'get',
  })
}

export function saveRoleSelMenu(data) {
  return request({
    url: '/ucenter/permission/save-role-sel',
    method: 'post',
    data
  })
}

export function removeById(params) {
  return request({
    url: `/ucenter/permission/remove/${params}`,
    method: 'delete',
  })
}

export function exportAll(params) {
  return request({
    url: '/ucenter/permission/export-all',
    method: 'get',
    responseType: 'blob',
    params
  })
}

export function exportPage(params) {
  return request({
    url: '/ucenter/permission/export',
    method: 'post',
    responseType: 'blob',
    params
  })
}
