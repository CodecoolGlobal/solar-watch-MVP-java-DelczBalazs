import axios from 'axios'

// Separate axios instance for AI service (no JWT attached)
const aiHttp = axios.create({
  baseURL: import.meta.env.VITE_AI_BASE_URL || '',
})

export default aiHttp
