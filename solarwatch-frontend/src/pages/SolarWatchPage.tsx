import { useState } from 'react'
import { z } from 'zod'
import { useForm } from 'react-hook-form'
import { getSolar } from '@/features/solar/solar.api'
import type { SolarResponse } from '@/features/solar/solar.types'
import { toast } from '@/components/ui/sonner'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import { Label } from '@/components/ui/label'
import { Input } from '@/components/ui/input'
import { Button } from '@/components/ui/button'
import { safeZodResolver } from '@/lib/safeZodResolver'

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
  const [result, setResult] = useState<SolarResponse | null>(null)
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
      const data = await getSolar(values.city, values.date)
      setResult(data)
    } catch (err: any) {
      setResult(null)
      toast.error('Failed to fetch solar data', {
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
          <form noValidate onSubmit={handleSubmit(onSubmit, onInvalid)} className="grid grid-cols-1 md:grid-cols-3 gap-4">
            <div className="md:col-span-1">
              <Label htmlFor="city">City</Label>
              <Input id="city" placeholder="Budapest" {...register('city')} />
              {errors.city && <p className="text-sm text-red-400 mt-1">{errors.city.message}</p>}
            </div>
            <div className="md:col-span-1">
              <Label htmlFor="date">Date (yyyy-mm-dd)</Label>
              <Input id="date" type="date" {...register('date')} />
              {errors.date && <p className="text-sm text-red-400 mt-1">{errors.date.message}</p>}
            </div>
            <div className="md:col-span-1 flex items-end">
              <Button type="submit" disabled={isSubmitting} className="w-full md:w-auto">
                {isSubmitting ? 'Searching...' : 'Search'}
              </Button>
            </div>
          </form>
        </CardContent>
      </Card>

      {!result ? (
        <div className="text-center text-white/70">Enter a city to see results.</div>
      ) : (
        <Card>
          <CardHeader>
            <CardTitle>
              {result.city}
              {result.country ? `, ${result.country}` : ''}
            </CardTitle>
            <CardDescription>
              {result.date} — {result.timezone}
            </CardDescription>
          </CardHeader>
          <CardContent>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div className="glass p-4">
                <div className="text-sm text-white/70">Sunrise</div>
                <div className="text-2xl font-semibold">
                  {new Date(result.sunrise).toLocaleTimeString()}
                </div>
              </div>
              <div className="glass p-4">
                <div className="text-sm text-white/70">Sunset</div>
                <div className="text-2xl font-semibold">
                  {new Date(result.sunset).toLocaleTimeString()}
                </div>
              </div>
            </div>
          </CardContent>
        </Card>
      )}
    </div>
  )
}
