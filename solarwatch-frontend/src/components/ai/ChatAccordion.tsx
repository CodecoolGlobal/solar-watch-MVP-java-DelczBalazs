import { Suspense, lazy, useState } from 'react'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import Loading from '@/components/loading/Loading'
import Collapsible from '@/components/ui/Collapsible'

const ChatPanel = lazy(() => import('./ChatPanel'))

interface Props {
  city?: string | null
}

export default function ChatAccordion({ city }: Props) {
  const [open, setOpen] = useState(false)

  return (
    <Card>
      <CardHeader className="flex flex-row items-center justify-between">
        <CardTitle>
          <span className="inline-flex items-center gap-2">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round" className="w-5 h-5 md:w-6 md:h-6 relative top-[1px]">
              <path d="M12 3v3" />
              <rect x="6" y="8" width="12" height="8" rx="2" />
              <circle cx="10" cy="12" r="1" />
              <circle cx="14" cy="12" r="1" />
              <path d="M8 16h8" />
              <path d="M5 11v2" />
              <path d="M19 11v2" />
            </svg>
            <span>AI Assistant</span>
          </span>
        </CardTitle>
        <Button
          type="button"
          variant="outline"
          onClick={() => setOpen((v) => !v)}
        >
          {open ? 'Hide' : 'Ask AI'}
        </Button>
      </CardHeader>
      <CardContent>
        <Collapsible open={open} unmountOnExit={false} durationMs={350}>
          <Suspense fallback={<div className="py-4 flex justify-center"><Loading /></div>}>
            <ChatPanel city={city} bare />
          </Suspense>
        </Collapsible>
      </CardContent>
    </Card>
  )
}
