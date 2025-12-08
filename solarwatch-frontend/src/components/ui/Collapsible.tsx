import { useEffect, useLayoutEffect, useRef, useState } from 'react'

interface Props {
  open: boolean
  children: React.ReactNode
  className?: string
  durationMs?: number
  lazyMount?: boolean
  unmountOnExit?: boolean
}

export default function Collapsible({ open, children, className = '', durationMs = 300, lazyMount = false, unmountOnExit = false }: Props) {
  const containerRef = useRef<HTMLDivElement>(null)
  const [mounted, setMounted] = useState(!lazyMount || open)
  const [height, setHeight] = useState<number | 'auto'>(open ? 'auto' : 0)

  useEffect(() => {
    if (open && !mounted) setMounted(true)
  }, [open, mounted])

  useLayoutEffect(() => {
    const el = containerRef.current
    if (!el) return

    if (open) {
      // Expand with reliable transition: 0 -> measured height, then 'auto' on transition end
      setHeight(0)
      // Force reflow to register starting height
      // eslint-disable-next-line @typescript-eslint/no-unused-expressions
      el.offsetHeight
      requestAnimationFrame(() => {
        setHeight(el.scrollHeight)
      })
    } else {
      // Collapse: auto/px -> measured px -> 0
      const sh = el.scrollHeight
      setHeight(sh)
      // Force reflow so the browser registers the starting height before we go to 0
      // eslint-disable-next-line @typescript-eslint/no-unused-expressions
      el.offsetHeight
      requestAnimationFrame(() => setHeight(0))
      if (unmountOnExit) {
        const id = window.setTimeout(() => setMounted(false), durationMs)
        return () => window.clearTimeout(id)
      }
    }
  }, [open, durationMs, unmountOnExit])

  // While opening and before we switch to 'auto', keep syncing height if content grows (lazy content loads)
  useEffect(() => {
    const el = containerRef.current
    if (!el) return
    if (!open || height === 'auto') return
    const ro = new ResizeObserver(() => {
      setHeight((h) => (h === 'auto' ? 'auto' : el.scrollHeight))
    })
    ro.observe(el)
    return () => ro.disconnect()
  }, [open, height])

  // After height animation completes, set to 'auto' to accommodate future content changes without jumps
  useEffect(() => {
    const el = containerRef.current
    if (!el) return
    const onEnd = (e: TransitionEvent) => {
      if (e.propertyName === 'height' && open) {
        setHeight('auto')
      }
    }
    el.addEventListener('transitionend', onEnd)
    return () => el.removeEventListener('transitionend', onEnd)
  }, [open])

  if (!mounted) return null

  const style: React.CSSProperties = {
    height: height === 'auto' ? 'auto' : `${height}px`,
    transition: `all ${durationMs}ms cubic-bezier(0.4, 0, 0.2, 1)`,
    overflow: 'hidden',
    willChange: 'height, opacity, transform',
  }

  const innerStyle: React.CSSProperties = {
    opacity: open ? 1 : 0,
    transform: open ? 'translateY(0)' : 'translateY(-6px)',
    transition: `opacity ${Math.min(durationMs, 250)}ms ease, transform ${durationMs}ms cubic-bezier(0.4, 0, 0.2, 1)`,
    willChange: 'opacity, transform',
  }

  return (
    <div ref={containerRef} style={style} className={className} aria-hidden={!open}>
      <div style={innerStyle}>{children}</div>
    </div>
  )
}
