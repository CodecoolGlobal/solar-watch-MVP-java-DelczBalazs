import { Navbar } from '@/components/Navbar'

export default function Layout({ children }: { children: React.ReactNode }) {
  return (
    <div className="min-h-screen bg-sunrise-gradient text-white">
      <header className="sticky top-0 z-40 backdrop-blur supports-[backdrop-filter]:bg-white/5">
        <Navbar />
      </header>
      <main className="container mx-auto max-w-4xl px-4 py-10">{children}</main>
      <footer className="py-8 text-center text-xs text-white/60">
        SolarWatch © {new Date().getFullYear()}
      </footer>
    </div>
  )
}
