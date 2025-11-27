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
import Loading from '@/components/loading/Loading'

const EMAIL_RE = /^(?!\.)(?!.*\.\.)[A-Za-z0-9_'+\-.]+@[A-Za-z0-9-]+(?:\.[A-Za-z0-9-]+)*\.[A-Za-z]{2,}$/
const FULLNAME_RE = /^[A-Za-zÀ-ÖØ-öø-ÿ][A-Za-zÀ-ÖØ-öø-ÿ'\- ]+[A-Za-zÀ-ÖØ-öø-ÿ]$/

const schema = z.object({
  fullName: z
    .string()
    .trim()
    .min(2, 'Full name is required')
    .max(80, 'Full name is too long')
    .regex(FULLNAME_RE, "Use letters, spaces, apostrophes and hyphens only"),
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

export default function RegisterPage() {
  const { register: apiRegister } = useAuth()
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
      await apiRegister(values.email, values.password, values.fullName)
      navigate(from, { replace: true })
    } catch (err: any) {
      toast.error('Registration failed', {
        description: err?.response?.data?.message || 'Please try again.',
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
          <CardTitle>Create account</CardTitle>
          <CardDescription>Start using SolarWatch</CardDescription>
        </CardHeader>
        <CardContent>
          {isSubmitting && (
            <div className="py-2 flex justify-center"><Loading /></div>
          )}
          <form noValidate onSubmit={handleSubmit(onSubmit, onInvalid)} className="space-y-4">
            <div>
              <Label htmlFor="fullName">Full name</Label>
              <Input id="fullName" {...register('fullName')} />
              {errors.fullName && <p className="text-sm text-red-400 mt-1">{errors.fullName.message}</p>}
            </div>
            <div>
              <Label htmlFor="email">Email</Label>
              <Input id="email" type="email" autoComplete="email" {...register('email')} />
              {errors.email && <p className="text-sm text-red-400 mt-1">{errors.email.message}</p>}
            </div>
            <div>
              <Label htmlFor="password">Password</Label>
              <Input id="password" type="password" autoComplete="new-password" {...register('password')} />
              {errors.password && <p className="text-sm text-red-400 mt-1">{errors.password.message}</p>}
            </div>
            <Button type="submit" disabled={isSubmitting} className="w-full">
              {isSubmitting ? 'Creating account...' : 'Create account'}
            </Button>
          </form>
        </CardContent>
        <CardFooter>
          <p className="text-sm text-white/70">
            Already have an account? <Link className="text-blue-400 underline" to="/login">Login</Link>
          </p>
        </CardFooter>
      </Card>
    </div>
  )
}
