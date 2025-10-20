import { createContext, useContext, useEffect, useMemo, useState, type ReactNode } from 'react'
import { STORAGE_KEYS } from '@/lib/utils'
import { login as apiLogin, register as apiRegister } from './auth.api'
import type { User } from './auth.types'

interface AuthContextValue {
  user: User | null
  token: string | null
  isAuthenticated: boolean
  login: (email: string, password: string) => Promise<void>
  register: (email: string, password: string, fullName?: string) => Promise<void>
  logout: () => void
}

const AuthContext = createContext<AuthContextValue | undefined>(undefined)

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<User | null>(null)
  const [token, setToken] = useState<string | null>(null)

  useEffect(() => {
    const t = localStorage.getItem(STORAGE_KEYS.TOKEN)
    setToken(t)
    setUser(null)
  }, [])

  useEffect(() => {
    const onUnauthorized = () => {
      setToken(null)
      setUser(null)
    }
    window.addEventListener('sw:unauthorized', onUnauthorized)
    return () => window.removeEventListener('sw:unauthorized', onUnauthorized)
  }, [])

  const login = async (email: string, password: string) => {
    const res = await apiLogin({ email, password })
    localStorage.setItem(STORAGE_KEYS.TOKEN, res.token)
    setToken(res.token)
    setUser(null)
  }

  const register = async (email: string, password: string, fullName?: string) => {
    await apiRegister({ email, password, fullName: fullName ?? '' })
    const res = await apiLogin({ email, password })
    localStorage.setItem(STORAGE_KEYS.TOKEN, res.token)
    setToken(res.token)
    setUser(null)
  }

  const logout = () => {
    localStorage.removeItem(STORAGE_KEYS.TOKEN)
    localStorage.removeItem(STORAGE_KEYS.USER)
    setToken(null)
    setUser(null)
  }

  const value = useMemo(
    () => ({
      user,
      token,
      isAuthenticated: !!token,
      login,
      register,
      logout,
    }),
    [user, token],
  )

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}

export function useAuth() {
  const ctx = useContext(AuthContext)
  if (!ctx) throw new Error('useAuth must be used within AuthProvider')
  return ctx
}
