import { z } from 'zod'
import { useForm } from 'react-hook-form'
import { useAuth } from '@/features/auth/AuthProvider'
import { useLocation, useNavigate, Link } from 'react-router-dom'
import { toast } from '@/components/ui/sonner'
import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from '@/components/ui/card'
import { Label } from '@/components/ui/label'
import { Input } from '@/components/ui/input'
import { Button } from '@/components/ui/button'
import { safeZodResolver } from '@/lib/safeZodResolver'

const EMAIL_RE = /^(?!\.)(?!.*\.\.)[A-Za-z0-9_'+\-.]+@[A-Za-z0-9-]+(?:\.[A-Za-z0-9-]+)*\.[A-Za-z]{2,}$/

const schema = z.object({
  email: z
    .string()
    .trim()
    .regex(EMAIL_RE, 'Invalid email address'),
  password: z
    .string()
    .min(6, 'Password must be at least 6 characters')
    .max(128, 'Password must be at most 128 characters')
    .refine((v) => !/\s/.test(v), 'Password cannot contain spaces'),
})

type FormValues = z.infer<typeof schema>

export default function LoginPage() {
  const { login } = useAuth()
  const navigate = useNavigate()
  const location = useLocation() as any
  const from = location.state?.from || '/solar-watch'

  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
  } = useForm<FormValues>({
    resolver: safeZodResolver(schema),
    mode: 'onTouched',
    reValidateMode: 'onChange',
    shouldFocusError: true,
  })

  const onSubmit = async (values: FormValues) => {
    try {
      await login(values.email, values.password)
      navigate(from, { replace: true })
    } catch (err: any) {
      toast.error('Login failed', {
        description: err?.response?.data?.message || 'Please check your credentials.',
      })
    }
  }

  const onInvalid = () => {
    toast.error('Please fix the form errors')
  }

  return (
    <div className="mx-auto max-w-md">
      <Card>
        <CardHeader>
          <CardTitle>Login</CardTitle>
          <CardDescription>Access your SolarWatch dashboard</CardDescription>
        </CardHeader>
        <CardContent>
          <form noValidate onSubmit={handleSubmit(onSubmit, onInvalid)} className="space-y-4">
            <div>
              <Label htmlFor="email">Email</Label>
              <Input id="email" type="email" autoComplete="email" {...register('email')} />
              {errors.email && <p className="text-sm text-red-400 mt-1">{errors.email.message}</p>}
            </div>
            <div>
              <Label htmlFor="password">Password</Label>
              <Input id="password" type="password" autoComplete="current-password" {...register('password')} />
              {errors.password && <p className="text-sm text-red-400 mt-1">{errors.password.message}</p>}
            </div>
            <Button type="submit" disabled={isSubmitting} className="w-full">
              {isSubmitting ? 'Signing in...' : 'Sign in'}
            </Button>
          </form>
        </CardContent>
        <CardFooter>
          <p className="text-sm text-white/70">
            No account? <Link className="text-blue-400 underline" to="/registration">Register</Link>
          </p>
        </CardFooter>
      </Card>
    </div>
  )
}
