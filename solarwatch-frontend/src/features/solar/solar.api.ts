import http from '@/lib/http'
import type { SolarResponse } from './solar.types'
import type { SolarWatchDashboard } from '@/types/solarwatch'

export async function getSolar(city: string, date?: string): Promise<SolarResponse> {
  const params: Record<string, string> = { city }
  if (date) params.date = String(date)
  const res = await http.get<SolarResponse>('/solarwatch', { params })
  return res.data
}

export async function getDashboard(city: string, date?: string): Promise<SolarWatchDashboard> {
  const params: Record<string, string> = { city }
  if (date) params.date = String(date)
  const res = await http.get<SolarWatchDashboard>('/solarwatch/dashboard', { params })
  return res.data
}
