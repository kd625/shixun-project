import request from '@/utils/request'

export function getDashboardSummary() {
  return request({ url: '/virtual/dashboard/summary', method: 'get' })
}
