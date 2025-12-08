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
      { index: true, element: <Navigate to="/dashboard" replace /> },
      { path: 'login', element: <LoginPage /> },
      { path: 'registration', element: <RegisterPage /> },
      // Legacy path redirect to new dashboard URL
      { path: 'solar-watch', element: <Navigate to="/dashboard" replace /> },
      {
        element: <ProtectedRoute />,
        children: [{ path: 'dashboard', element: <SolarWatchPage /> }],
      },
    ],
  },
])
