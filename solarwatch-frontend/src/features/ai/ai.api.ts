import aiHttp from '@/lib/http-ai'

export interface AiChatRequest {
  city?: string | null
  question: string
}

export interface AiChatResponse {
  answer: string
}

export async function askAi(city: string | null | undefined, question: string): Promise<string> {
  const payload: AiChatRequest = { city: city ?? undefined, question }
  const res = await aiHttp.post<AiChatResponse>('/api/ai/chat', payload)
  return res.data.answer
}
