import request from '@/utils/request'

export function save(data) {
    return request({
      url: '/edu/course/save',
      method: 'post',
      data
    })
  }

  export function getTree(data) {
    return request({
      url: '/edu/course/tree',
      method: 'post',
      data
    })
  }

  export function getList(data) {
    return request({
      url: '/edu/course/list',
      method: 'post',
      data
    })
  }

  export function getPage(params) {
    return request({
      url: '/edu/course/page',
      method: 'post',
      params
    })
  }

  export function getListByTeacherId(id) {
    return request({
      url: '/edu/course/list-teacher',
      method: 'post',
      params: {id}
    })
  }