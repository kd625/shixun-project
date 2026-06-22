import request from '@/utils/request'

export function getPortalHome() {
  return request({ url: '/portal/home', method: 'get', headers: { isToken: false } })
}

export function listPortalNews(query) {
  return request({ url: '/portal/news', method: 'get', params: query, headers: { isToken: false } })
}

export function listPortalResources(query) {
  return request({ url: '/portal/resources', method: 'get', params: query, headers: { isToken: false } })
}

export function listPortalLabs(query) {
  return request({ url: '/portal/labs', method: 'get', params: query, headers: { isToken: false } })
}

export function listPortalExperiments(query) {
  return request({ url: '/portal/experiments', method: 'get', params: query, headers: { isToken: false } })
}

export function getPortalScreen() {
  return request({ url: '/portal/screen', method: 'get', headers: { isToken: false } })
}

export function addPortalShare(data) {
  return request({ url: '/portal/share', method: 'post', data: data, headers: { isToken: false } })
}
