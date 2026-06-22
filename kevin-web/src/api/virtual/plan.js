import request from '@/utils/request'

export function listPlan(query) {
  return request({ url: '/virtual/plan/list', method: 'get', params: query })
}

export function getPlan(planId) {
  return request({ url: '/virtual/plan/' + planId, method: 'get' })
}

export function addPlan(data) {
  return request({ url: '/virtual/plan', method: 'post', data: data })
}

export function updatePlan(data) {
  return request({ url: '/virtual/plan', method: 'put', data: data })
}

export function delPlan(planId) {
  return request({ url: '/virtual/plan/' + planId, method: 'delete' })
}

export function optionselectPlan() {
  return request({ url: '/virtual/plan/optionselect', method: 'get' })
}
