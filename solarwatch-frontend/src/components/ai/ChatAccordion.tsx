import { Suspense, lazy, useState } from 'react'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import Loading from '@/components/loading/Loading'

const ChatPanel = lazy(() => import('./ChatPanel'))

interface Props {
  city?: string | null
}

export default function ChatAccordion({ city }: Props) {
  const [open, setOpen] = useState(false)
  const [initialized, setInitialized] = useState(false)

  return (
    <Card>
      <CardHeader className="flex flex-row items-center justify-between">
        <CardTitle>AI Assistant</CardTitle>
        <Button
          type="button"
          variant="outline"
          onClick={() => {
            setOpen((v) => {
              const next = !v
              if (next && !initialized) setInitialized(true)
              return next
            })
          }}
        >
          {open ? 'Hide' : 'Ask AI'}
        </Button>
      </CardHeader>
      <CardContent className={open ? '' : 'hidden'}>
        {initialized && (
          <Suspense fallback={<div className="py-4 flex justify-center"><Loading /></div>}>
            <ChatPanel city={city} bare />
          </Suspense>
        )}
      </CardContent>
    </Card>
  )
}
