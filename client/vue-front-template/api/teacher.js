import request from '@/utils/request'

export function getList(params) {
  return request({
    url: '/edu/edu-teacher/list',
    method: 'get',
    params
  })
}

export function getClientTeacherList(params) {
  return request({
    url: '/edu/edu-teacher/client-list',
    method: 'get',
    params
  })
}

export function getAll() {
  return request({
    url: '/edu/edu-teacher/client-all',
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

export function save(data) {
  return request({
    url: '/edu/edu-teacher/save',
    method: 'post',
    data
  })
}

