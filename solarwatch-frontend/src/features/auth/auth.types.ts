export interface User {
  id: string
  email: string
  fullName: string
  roles: string[]
}

export interface AuthResponse {
  token: string
}

export interface LoginRequest {
  email: string
  password: string
}

export interface RegisterRequest {
  email: string
  password: string
  fullName: string
}
