import request from '@/utils/request'

export function getList(params) {
  return request({
    url: '/edu/edu-teacher/page/whi',
    method: 'get',
    params
  })
}

export function exportPage(params) {
  return request({
    url: '/edu/edu-teacher/export',
    method: 'get',
    responseType: 'blob',
    params
  })
}

export function getAll() {
  return request({
    url: '/edu/edu-teacher/all/whi',
    method: 'get',
  })
}

export function getAllAndBindId(params) {
  return request({
    url: `/edu/edu-teacher/all-bind/${params}/whi`,
    method: 'get',
  })
}

export function removeById(params) {
  return request({
    url: `/edu/edu-teacher/remove/${params}`,
    method: 'delete',
  })
}

export function getById(params) {
  return request({
    url: `/edu/edu-teacher/info/${params}`,
    method: 'get',
  })
}

export function updateById(data) {
  return request({
    url: '/edu/edu-teacher/edit',
    method: 'put',
    data
  })
}

export function updateBindByUidAndId(data) {
  return request({
    url: '/edu/edu-teacher/update-bind',
    method: 'put',
    data
  })
}

export function save(data) {
  return request({
    url: '/edu/edu-teacher/save',
    method: 'post',
    data
  })
}

