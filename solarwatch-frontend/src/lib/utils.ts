import { type ClassValue, clsx } from 'clsx'
import { twMerge } from 'tailwind-merge'

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs))
}

export const STORAGE_KEYS = {
  TOKEN: 'sw.auth.token',
  USER: 'sw.auth.user',
} as const
