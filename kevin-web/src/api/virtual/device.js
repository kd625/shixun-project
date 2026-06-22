import request from '@/utils/request'

export function listDevice(query) {
  return request({ url: '/virtual/device/list', method: 'get', params: query })
}

export function getDevice(deviceId) {
  return request({ url: '/virtual/device/' + deviceId, method: 'get' })
}

export function addDevice(data) {
  return request({ url: '/virtual/device', method: 'post', data: data })
}

export function updateDevice(data) {
  return request({ url: '/virtual/device', method: 'put', data: data })
}

export function delDevice(deviceId) {
  return request({ url: '/virtual/device/' + deviceId, method: 'delete' })
}
