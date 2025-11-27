import { Link, useLocation } from 'react-router-dom'
import { useAuth } from '@/features/auth/AuthProvider'
import { Badge } from '@/components/ui/badge'
import { cn } from '@/lib/utils'

const linkBtn = cn(
  'inline-flex items-center justify-center rounded-md font-medium transition-colors focus-visible:outline-none disabled:opacity-50 disabled:pointer-events-none h-10 px-4 text-sm',
)
const ghost = cn(linkBtn, 'border border-white/10 bg-transparent hover:bg-white/5 cursor-pointer')
const primary = cn(linkBtn, 'bg-blue-600 text-white hover:bg-blue-500')

export function Navbar() {
  const { isAuthenticated, logout, user } = useAuth()
  const location = useLocation()
  const from = location.pathname + location.search

  return (
    <nav className="container mx-auto max-w-4xl px-4 py-4 flex items-center justify-between">
      <Link to="/" className="font-semibold">
        SolarWatch
      </Link>
      <div className="flex items-center gap-2">
        {!isAuthenticated ? (
          <>
            <Link className={ghost} to="/login" state={{ from }}>
              Login
            </Link>
            <Link className={primary} to="/registration">
              Register
            </Link>
          </>
        ) : (
          <>
            <Link className={ghost} to="/solar-watch">
              Dashboard
            </Link>
            {user ? <Badge className="hidden sm:inline">{user.fullName}</Badge> : null}
            <button className={ghost} onClick={logout}>
              Logout
            </button>
          </>
        )}
      </div>
    </nav>
  )
}
