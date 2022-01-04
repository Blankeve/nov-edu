import request from '@/utils/request'

export function save(data) {
    return request({
      url: '/edu/course/save',
      method: 'post',
      data
    })
  }

  export function removeById(params) {
    return request({
      url: `/edu/course/remove/${params}`,
      method: 'delete',
    })
  }

  export function getTree(params) {
    return request({
      url: '/edu/course/tree',
      method: 'post',
      params
    })
  }

  export function getList(data) {
    return request({
      url: '/edu/course/list',
      method: 'get',
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

  export function getOneByCourseId(id) {
    return request({
      url: '/edu/course/id',
      method: 'post',
      params: {id}
    })
  }

  export function getOneDetailByCourseId(params) {
    return request({
      url: `/edu/course/detail/${params}`,
      method: 'post',
    })
  }

  export function getIntroByCourseId(params) {
    return request({
      url: `/edu/course-intro/${params}`,
      method: 'get',
    })
  }

  export function getListByTeacherId(id) {
    return request({
      url: '/edu/course/list-teacher',
      method: 'post',
      params: {id}
    })
  }