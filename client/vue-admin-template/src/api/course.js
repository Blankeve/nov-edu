import request from '@/utils/request'

export function save(data) {
    return request({
      url: '/edu/course/save',
      method: 'post',
      data
    })
  }

  export function release(data) {
    return request({
      url: '/edu/course/release',
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
      url: '/edu/course/drop-list/whi',
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

  export function exportPage(params) {
    return request({
      url: '/edu/course/export',
      method: 'get',
      responseType: 'blob',
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
      url: `/edu/course/detail/${params}/whi`,
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
      url: '/edu/course/list-teacher/whi',
      method: 'post',
      params: {id}
    })
  }

  export function getRecentAddCourses() {
    return request({
      url: '/edu/course/recent-courses',
      method: 'get',
    })
  }
  