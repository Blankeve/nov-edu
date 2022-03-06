import request from '@/utils/request'

export function getList() {
  return request({
    url: '/edu/subject/list',
    method: 'get',
  })
}

export function getDashboardInfo() {
  return request({
    url: '/edu/subject/dashboard-info',
    method: 'get',
  })
}

export function getParentList(params) {
  return request({
    url: `/edu/subject/list-parent/${params}`,
    method: 'get',
  })
}

export function update(data) {
  return request({
    url: '/edu/subject/update',
    method: 'put',
    data
  })
}

export function saveOrUpdate(data) {
  return request({
    url: '/edu/subject/save-update',
    method: 'post',
    data
  })
}

export function removeSubjectById(params) {
  return request({
    url: `/edu/subject/remove/${params}`,
    method: 'delete',
  })
}