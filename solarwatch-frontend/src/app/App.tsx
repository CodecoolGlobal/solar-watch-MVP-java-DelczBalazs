import Layout from '@/components/Layout'
import { Outlet } from 'react-router-dom'
import { Toaster } from '@/components/ui/sonner'

export default function App() {
  return (
    <>
      <Layout>
        <Outlet />
      </Layout>
      <Toaster
        position="bottom-right"
        theme="dark"
        richColors
        closeButton
        toastOptions={{
          classNames: {
            toast: 'rounded-lg ring-1 ring-white/10 backdrop-blur-md text-white shadow-xl',
            title: 'text-white',
            description: 'text-white/80',
            actionButton: 'bg-white/10 hover:bg-white/20 text-white',
            cancelButton: 'bg-transparent text-white/60 hover:text-white',
          },
        }}
      />
    </>
  )
}
