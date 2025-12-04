import { useEffect, useMemo, useRef, useState } from 'react'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Input } from '@/components/ui/input'
import { Button } from '@/components/ui/button'
import { askAi } from '@/features/ai/ai.api'
import Loading from '@/components/loading/Loading'

interface Message {
  role: 'user' | 'assistant'
  content: string
}

interface Props {
  city?: string | null
  bare?: boolean
}

export default function ChatPanel({ city, bare = false }: Props) {
  const [messages, setMessages] = useState<Message[]>([])
  const [question, setQuestion] = useState('')
  const [isSending, setIsSending] = useState(false)
  const scrollerRef = useRef<HTMLDivElement>(null)
  const sendingRef = useRef(false)

  const suggestions = useMemo(() => {
    const c = (city || '').trim()
    return [
      c ? `What's the best time for sunrise photos in ${c}?` : 'What is a good time for sunrise photos? ',
      c ? `How windy is it usually in ${c} today?` : 'How windy is it today?',
      c ? `Tell me a fun fact about ${c}.` : 'Tell me a fun fact about sunrise.',
      'Explain the relation between daylight length and seasons.',
    ]
  }, [city])

  const send = async (text: string) => {
    if (sendingRef.current) return
    const q = text.trim()
    if (!q) return
    sendingRef.current = true
    setIsSending(true)
    setMessages((prev) => [...prev, { role: 'user', content: q }])
    setQuestion('')
    // scroll after user message
    setTimeout(() => scrollerRef.current?.scrollTo({ top: scrollerRef.current.scrollHeight, behavior: 'smooth' }), 0)
    try {
      const answer = await askAi(city ?? null, q)
      setMessages((prev) => [...prev, { role: 'assistant', content: answer }])
      // scroll to bottom
      setTimeout(() => scrollerRef.current?.scrollTo({ top: scrollerRef.current.scrollHeight, behavior: 'smooth' }), 50)
    } catch (e) {
      setMessages((prev) => [...prev, { role: 'assistant', content: 'Sorry, the AI service is unavailable right now.' }])
    } finally {
      setIsSending(false)
      sendingRef.current = false
    }
  }

  const onSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    void send(question)
  }

  // Ensure auto-scroll on any message change
  useEffect(() => {
    scrollerRef.current?.scrollTo({ top: scrollerRef.current.scrollHeight, behavior: 'smooth' })
  }, [messages])

  const inner = (
    <>
      {/* suggestions */}
      <div className={"flex flex-wrap gap-2 mb-4 " + (isSending ? 'pointer-events-none' : '')}>
        {suggestions.map((s, i) => (
          <button
            key={i}
            type="button"
            onClick={() => send(s)}
            disabled={isSending}
            className={
              'text-xs px-2 py-1 rounded-full text-white ' +
              (isSending
                ? 'bg-white/10 opacity-60 cursor-not-allowed'
                : 'bg-white/10 hover:bg-white/20')
            }
          >
            {s}
          </button>
        ))}
      </div>

      {/* messages */}
      <div className="glass h-64 md:h-72 mb-4 rounded-2xl overflow-hidden">
        <div ref={scrollerRef} className="h-full overflow-y-auto p-3 space-y-3 dark-scrollbar">
          {messages.length === 0 ? (
            <div className="text-sm text-white/70">Ask me about your selected city or anything else.</div>
          ) : (
            messages.map((m, idx) => (
              <div key={idx} className={m.role === 'user' ? 'flex justify-end' : 'flex justify-start'}>
                <div
                  className={
                    'max-w-[85%] px-3 py-2 rounded-lg ' +
                    (m.role === 'user' ? 'bg-blue-600 text-white' : 'bg-white/10 ring-1 ring-white/10 text-white')
                  }
                >
                  {m.content}
                </div>
              </div>
            ))
          )}
        </div>
      </div>

      {/* composer */}
      <form onSubmit={onSubmit} className="flex gap-2 items-center">
        <Input
          value={question}
          onChange={(e) => setQuestion(e.target.value)}
          placeholder={city ? `Ask about ${city} or anything...` : 'Ask something...'}
          className="flex-1"
          disabled={isSending}
        />
        <Button type="submit" disabled={isSending || question.trim() === ''}>
          {isSending ? <span className="flex items-center gap-2"><Loading /> Sending...</span> : 'Send'}
        </Button>
      </form>
    </>
  )

  if (bare) {
    return inner
  }

  return (
    <Card>
      <CardHeader>
        <CardTitle>AI Assistant</CardTitle>
      </CardHeader>
      <CardContent>{inner}</CardContent>
    </Card>
  )
}
