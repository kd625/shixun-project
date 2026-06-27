import { getToken } from '@/utils/auth'

function parseSseBlock(block) {
  const event = { event: 'message', data: '' }
  block.split('\n').forEach(line => {
    if (line.startsWith('event:')) {
      event.event = line.slice(6).trim()
    } else if (line.startsWith('data:')) {
      event.data += line.slice(5).trim()
    }
  })
  return event
}

function getBaseUrl() {
  return process.env.VUE_APP_BASE_API || ''
}

export async function streamGenerateIntro(payload, handlers) {
  const controller = new AbortController()
  const token = getToken()
  let finished = false

  const finish = () => {
    if (finished) {
      return
    }
    finished = true
    if (handlers && handlers.done) {
      handlers.done()
    }
  }

  const response = await fetch(getBaseUrl() + '/virtual/ai/generateIntro/stream', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json;charset=utf-8',
      'Accept': 'text/event-stream',
      'Authorization': token ? 'Bearer ' + token : ''
    },
    body: JSON.stringify(payload),
    signal: controller.signal
  })

  if (!response.ok) {
    throw new Error('AI生成接口异常：' + response.status)
  }

  const reader = response.body.getReader()
  const decoder = new TextDecoder('utf-8')
  let buffer = ''

  const read = async() => {
    const result = await reader.read()
    if (result.done) {
      finish()
      return
    }
    buffer += decoder.decode(result.value, { stream: true }).replace(/\r\n/g, '\n')
    const blocks = buffer.split('\n\n')
    buffer = blocks.pop()
    blocks.forEach(block => {
      if (!block.trim()) {
        return
      }
      const event = parseSseBlock(block)
      if (event.event === 'error' && handlers && handlers.error) {
        handlers.error(event.data || 'AI生成失败，请稍后重试')
      } else if (event.event === 'done') {
        finish()
      } else if (handlers && handlers.message) {
        handlers.message(event.data)
      }
    })
    await read()
  }

  read().catch(error => {
    if (error.name === 'AbortError') {
      return
    }
    if (handlers && handlers.error) {
      handlers.error(error.message || 'AI生成失败，请稍后重试')
    }
  })

  return controller
}
