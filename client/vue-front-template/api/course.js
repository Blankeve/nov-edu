import request from '@/utils/request'

export function save(data) {
    return request({
      url: '/edu/course/save',
      method: 'post',
      data
    })
  }

  export function applyCourse(data) {
    return request({
      url: '/edu/course-apply/save',
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
      url: '/edu/course/client-tree',
      method: 'post',
      params
    })
  }

  export function getList(data) {
    return request({
      url: '/edu/course/client-list',
      method: 'get',
      data
    })
  }

  export function getClientCourseList(data) {
    return request({
      url: '/edu/course/client-list',
      method: 'get',
      data
    })
  }

  export function getClientCourseApplyList(data) {
    return request({
      url: '/edu/course/client-apply',
      method: 'get',
      data
    })
  }

  export function getClientCourseBoughtList(data) {
    return request({
      url: '/edu/course/client-bought',
      method: 'get',
      data
    })
  }

  export function getPage(params) {
    return request({
      url: '/edu/course/page-client',
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