import http from '@/lib/http'
import type { AuthResponse, LoginRequest, RegisterRequest } from './auth.types'

export async function login(data: LoginRequest): Promise<AuthResponse> {
  const res = await http.post<AuthResponse>('/auth/login', data)
  return res.data
}

export async function register(data: RegisterRequest): Promise<void> {
  await http.post<void>('/auth/register', data)
}
