import { createBrowserRouter, Navigate } from 'react-router-dom'
import App from '@/app/App'
import LoginPage from '@/pages/LoginPage'
import RegisterPage from '@/pages/RegisterPage'
import SolarWatchPage from '@/pages/SolarWatchPage'
import ProtectedRoute from '@/components/ProtectedRoute'

export const router = createBrowserRouter([
  {
    path: '/',
    element: <App />,
    children: [
      { index: true, element: <Navigate to="/solar-watch" replace /> },
      { path: 'login', element: <LoginPage /> },
      { path: 'registration', element: <RegisterPage /> },
      {
        element: <ProtectedRoute />,
        children: [{ path: 'solar-watch', element: <SolarWatchPage /> }],
      },
    ],
  },
])
