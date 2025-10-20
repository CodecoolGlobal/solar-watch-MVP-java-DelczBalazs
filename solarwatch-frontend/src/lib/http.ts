import axios from 'axios'
import { STORAGE_KEYS } from '@/lib/utils'

const http = axios.create({
  baseURL: import.meta.env.DEV ? '/api' : (import.meta.env.VITE_API_BASE_URL || '/api'),
})

http.interceptors.request.use((config) => {
  const url = (config.url || '').toString().toLowerCase()
  // Never send Authorization to auth endpoints (remove if present)
  if (url.includes('/auth/')) {
    if (config.headers && 'Authorization' in config.headers) delete (config.headers as any).Authorization
    return config
  }

  const token = localStorage.getItem(STORAGE_KEYS.TOKEN) || ''
  const isJwt = token.split('.').length === 3 && token.indexOf('undefined') === -1 && token.indexOf('null') === -1
  if (isJwt) {
    config.headers = config.headers ?? {}
    config.headers.Authorization = `Bearer ${token}`
  } else {
    // ensure no stale header remains
    if (config.headers && 'Authorization' in config.headers) delete (config.headers as any).Authorization
  }
  return config
})

http.interceptors.response.use(
  (res) => res,
  (error) => {
    const status = error?.response?.status
    if (status === 401) {
      try {
        localStorage.removeItem(STORAGE_KEYS.TOKEN)
        localStorage.removeItem(STORAGE_KEYS.USER)
      } catch {}
      window.dispatchEvent(new Event('sw:unauthorized'))
    }
    return Promise.reject(error)
  },
)

export default http
