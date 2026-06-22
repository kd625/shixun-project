import request from '@/utils/request'

export function listResource(query) {
  return request({ url: '/virtual/resource/list', method: 'get', params: query })
}

export function getResource(resourceId) {
  return request({ url: '/virtual/resource/' + resourceId, method: 'get' })
}

export function addResource(data) {
  return request({ url: '/virtual/resource', method: 'post', data: data })
}

export function updateResource(data) {
  return request({ url: '/virtual/resource', method: 'put', data: data })
}

export function delResource(resourceId) {
  return request({ url: '/virtual/resource/' + resourceId, method: 'delete' })
}

export function optionselectResource() {
  return request({ url: '/virtual/resource/optionselect', method: 'get' })
}
