import request from '@/utils/request'



export function removeById(params) {
  return request({
    url: `/edu/chapter/remove/${params}`,
    method: 'delete',
  })
}

export function save(data) {
    return request({
      url: '/edu/chapter/save',
      method: 'post',
      data
    })
  }

  export function getPage(params) {
    return request({
      url: '/edu/chapter/page',
      method: 'post',
      params
    })
  }

  export function getChaptersByCourseId(id) {
    return request({
      url: '/edu/chapter/list-course',
      method: 'post',
      params: {id}
    })
  }