import request from '@/utils/request'

export function saveVideo(data) {
  return request({
    url: '/edu/video/save',
    method: 'post',
    data
  })
}

export function exportAll() {
  return request({
    url: '/edu/video/export-all',
    method: 'get',
    responseType: 'blob'
  })
}

export function exportPage(params) {
  return request({
    url: '/edu/video/export',
    method: 'post',
    responseType: 'blob',
    params
  })
}

export function getPage(params) {
  return request({
    url: '/edu/video/page',
    method: 'post',
    params
  })
}

export function getStudyRecordPage(params) {
  return request({
    url: '/edu/video/study/record/page',
    method: 'post',
    params
  })
}

export function getOneByVideoId(params) {
  return request({
    url: `/edu/video/detail/${params}`,
    method: 'post',
  })
}

export function removeVideoById(params) {
  return request({
    url: `/edu/video/remove/${params}`,
    method: 'delete',
  })
}