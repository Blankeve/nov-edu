import request from '@/utils/request'



export function getStudyRecordPage(params) {
  return request({
    url: '/edu/study/record/page',
    method: 'get',
    params
  })
}

export function exportPage(params) {
  return request({
    url: '/edu/study/export',
    method: 'get',
    responseType: 'blob',
    params
  })
}