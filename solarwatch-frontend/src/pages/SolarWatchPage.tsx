import { useState } from 'react'
import { z } from 'zod'
import { useForm } from 'react-hook-form'
import { getDashboard } from '@/features/solar/solar.api'
import type { SolarWatchDashboard } from '@/types/solarwatch'
import { toast } from '@/components/ui/sonner'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import { Label } from '@/components/ui/label'
import { Input } from '@/components/ui/input'
import { Button } from '@/components/ui/button'
import { safeZodResolver } from '@/lib/safeZodResolver'
import WeatherSummaryCards from '@/components/solarwatch/WeatherSummaryCards'
import CityMapEmbed from '@/components/solarwatch/CityMapEmbed'
import Loading from '@/components/loading/Loading'

const CITY_RE = /^[A-Za-zÀ-ÖØ-öø-ÿ][A-Za-zÀ-ÖØ-öø-ÿ' .-]*[A-Za-zÀ-ÖØ-öø-ÿ]$/
const DATE_RE = /^\d{4}-\d{2}-\d{2}$/

const schema = z.object({
  city: z
    .string()
    .trim()
    .min(1, 'City is required')
    .max(60, 'City name is too long')
    .regex(CITY_RE, "Use letters, spaces, apostrophes, hyphens or dot"),
  date: z
    .preprocess((v) => (v === '' || v == null ? undefined : v), z.string().regex(DATE_RE, 'Date must be yyyy-mm-dd').optional()),
})

type FormValues = z.infer<typeof schema>

function todayStr() {
  const d = new Date()
  const yyyy = d.getFullYear()
  const mm = String(d.getMonth() + 1).padStart(2, '0')
  const dd = String(d.getDate()).padStart(2, '0')
  return `${yyyy}-${mm}-${dd}`
}

export default function SolarWatchPage() {
  const [dashboard, setDashboard] = useState<SolarWatchDashboard | null>(null)
  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
  } = useForm<FormValues>({
    resolver: safeZodResolver(schema),
    mode: 'onTouched',
    reValidateMode: 'onChange',
    shouldFocusError: true,
    defaultValues: { date: todayStr() },
  })

  const onSubmit = async (values: FormValues) => {
    try {
      const data = await getDashboard(values.city, values.date)
      setDashboard(data)
    } catch (err: any) {
      setDashboard(null)
      toast.error('Failed to fetch dashboard data', {
        description: err?.response?.data?.message || 'Try another city.',
      })
    }
  }

  const onInvalid = () => {
    toast.error('Please fix the form errors')
  }

  return (
    <div className="space-y-6">
      <Card>
        <CardHeader>
          <CardTitle>SolarWatch</CardTitle>
          <CardDescription>Find sunrise and sunset times for your city.</CardDescription>
        </CardHeader>
        <CardContent>
          <form noValidate onSubmit={handleSubmit(onSubmit, onInvalid)} className="grid grid-cols-1 md:grid-cols-3 gap-4 items-end">
            <div className="md:col-span-1">
              <Label htmlFor="city">City</Label>
              <Input id="city" placeholder="Budapest" {...register('city')} />
              <div className="mt-1 min-h-10">
                {errors.city && (
                  <p className="text-sm text-red-400">{errors.city.message}</p>
                )}
              </div>
            </div>
            <div className="md:col-span-1">
              <Label htmlFor="date">Date (yyyy-mm-dd)</Label>
              <Input id="date" type="date" {...register('date')} />
              <div className="mt-1 min-h-10">
                {errors.date && (
                  <p className="text-sm text-red-400">{errors.date.message}</p>
                )}
              </div>
            </div>
            <div className="md:col-span-1 flex flex-col justify-end">
              {/* placeholder to match label height */}
              <div className="text-sm font-medium opacity-0 select-none">Search</div>
              <Button type="submit" disabled={isSubmitting} className="w-full md:w-auto">
                {isSubmitting ? 'Searching...' : 'Search'}
              </Button>
              <div className="mt-1 min-h-10" />
            </div>
          </form>
        </CardContent>
      </Card>

      {isSubmitting ? (
        <div className="py-8 flex justify-center"><Loading /></div>
      ) : !dashboard ? (
        <div className="text-center text-white/70">Enter a city to see results.</div>
      ) : (
        <Card>
          <CardHeader>
            <CardTitle>
              {dashboard.city}
              {dashboard.country ? `, ${dashboard.country}` : ''}
            </CardTitle>
            <CardDescription>
              {dashboard.date} — {dashboard.timezone}
            </CardDescription>
          </CardHeader>
          <CardContent>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div className="glass p-4">
                <div className="text-sm text-white/70">Sunrise</div>
                <div className="text-2xl font-semibold">
                  {new Date(dashboard.sunrise).toLocaleTimeString()}
                </div>
              </div>
              <div className="glass p-4">
                <div className="text-sm text-white/70">Sunset</div>
                <div className="text-2xl font-semibold">
                  {new Date(dashboard.sunset).toLocaleTimeString()}
                </div>
              </div>
            </div>
          </CardContent>
        </Card>
      )}

      {!isSubmitting && dashboard && (
        <div className="space-y-6">
          <WeatherSummaryCards weather={dashboard.weather} />
          <CityMapEmbed cityName={`${dashboard.city}${dashboard.country ? ', ' + dashboard.country : ''}`} />
        </div>
      )}
    </div>
  )
}
