import request from '@/utils/request'

export function listShareApply(query) {
  return request({ url: '/virtual/shareApply/list', method: 'get', params: query })
}

export function getShareApply(applyId) {
  return request({ url: '/virtual/shareApply/' + applyId, method: 'get' })
}

export function addShareApply(data) {
  return request({ url: '/virtual/shareApply', method: 'post', data: data })
}

export function updateShareApply(data) {
  return request({ url: '/virtual/shareApply', method: 'put', data: data })
}

export function delShareApply(applyId) {
  return request({ url: '/virtual/shareApply/' + applyId, method: 'delete' })
}

export function auditShareApply(data) {
  return request({ url: '/virtual/shareApply/audit', method: 'put', data: data })
}
