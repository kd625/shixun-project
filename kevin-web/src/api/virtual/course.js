import request from '@/utils/request'

export function listCourse(query) {
  return request({ url: '/virtual/course/list', method: 'get', params: query })
}

export function getCourse(courseId) {
  return request({ url: '/virtual/course/' + courseId, method: 'get' })
}

export function addCourse(data) {
  return request({ url: '/virtual/course', method: 'post', data: data })
}

export function updateCourse(data) {
  return request({ url: '/virtual/course', method: 'put', data: data })
}

export function delCourse(courseId) {
  return request({ url: '/virtual/course/' + courseId, method: 'delete' })
}

export function optionselectCourse() {
  return request({ url: '/virtual/course/optionselect', method: 'get' })
}
