import request from '@/utils/request'

export function listLab(query) {
  return request({ url: '/virtual/lab/list', method: 'get', params: query })
}

export function getLab(labId) {
  return request({ url: '/virtual/lab/' + labId, method: 'get' })
}

export function addLab(data) {
  return request({ url: '/virtual/lab', method: 'post', data: data })
}

export function updateLab(data) {
  return request({ url: '/virtual/lab', method: 'put', data: data })
}

export function delLab(labId) {
  return request({ url: '/virtual/lab/' + labId, method: 'delete' })
}

export function optionselectLab() {
  return request({ url: '/virtual/lab/optionselect', method: 'get' })
}
