import request from '@/utils/request'

export function login(param) {
  return request({
    url: '/user/login',
    method: 'post',
    data: param
  })
}

export function register(param) {
  return request({
    url: '/user/register',
    method: 'post',
    data: param
  })
}

export function getInfo(id) {
  return request({
    url: '/user/getInfo',
    method: 'get',
    params: { id }
  })
}

