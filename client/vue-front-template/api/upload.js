import request from '@/utils/request'

export function uploadImgBase64(data) {
  return request({
    url: '/upload/img/ba64',
    method: 'post',
    data
  })
}

