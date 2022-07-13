import request from '@/utils/request'

export function getList(params) {
  return request({
    url: '/ucenter/config/list',
    method: 'get',
    params
  })
}

export function getListByKey(params) {
  return request({
    url: '/ucenter/config/list',
    method: 'post',
    params
  })
}

export function getAll() {
  return request({
    url: '/ucenter/edu-teacher/all',
    method: 'get',
  })
}

export function removeById(params) {
  return request({
    url: `/ucenter/config/remove/${params}`,
    method: 'delete',
  })
}

export function saveOrUpdate(params) {
  return request({
    url: '/ucenter/config/save',
    method: 'post',
    params
  })
}

