import request from '@/utils/request'

export function listRecord(query) {
  return request({ url: '/virtual/record/list', method: 'get', params: query })
}

export function getRecord(recordId) {
  return request({ url: '/virtual/record/' + recordId, method: 'get' })
}

export function addRecord(data) {
  return request({ url: '/virtual/record', method: 'post', data: data })
}

export function updateRecord(data) {
  return request({ url: '/virtual/record', method: 'put', data: data })
}

export function delRecord(recordId) {
  return request({ url: '/virtual/record/' + recordId, method: 'delete' })
}
