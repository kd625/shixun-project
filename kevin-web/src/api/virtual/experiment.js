import request from '@/utils/request'

export function listExperiment(query) {
  return request({ url: '/virtual/experiment/list', method: 'get', params: query })
}

export function getExperiment(experimentId) {
  return request({ url: '/virtual/experiment/' + experimentId, method: 'get' })
}

export function addExperiment(data) {
  return request({ url: '/virtual/experiment', method: 'post', data: data })
}

export function updateExperiment(data) {
  return request({ url: '/virtual/experiment', method: 'put', data: data })
}

export function delExperiment(experimentId) {
  return request({ url: '/virtual/experiment/' + experimentId, method: 'delete' })
}

export function optionselectExperiment() {
  return request({ url: '/virtual/experiment/optionselect', method: 'get' })
}
