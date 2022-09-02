import request from '@/utils/request'

export function uploadImgBase64(data) {
  return request({
    url: '/upload/img/ba64/whi',
    method: 'post',
    data
  })
}

