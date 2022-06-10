import request from '@/utils/request'

export function getList(params) {
  return request({
    url: '/edu/config/list',
    method: 'get',
    params
  })
}

export function getListByKey(params) {
  return request({
    url: '/edu/config/list',
    method: 'post',
    params
  })
}

export function getAll() {
  return request({
    url: '/edu/edu-teacher/all',
    method: 'get',
  })
}

export function removeById(params) {
  return request({
    url: `/edu/config/remove/${params}`,
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

export function saveOrUpdate(params) {
  return request({
    url: '/edu/config/save',
    method: 'post',
    params
  })
}

export function changeStatus(params) {
  return request({
    url: '/edu/config/change/status',
    method: 'post',
    params
  })
}