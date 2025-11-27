import { Card, CardContent } from '@/components/ui/card'
import Loading from '@/components/loading/Loading'
import { useEffect, useState } from 'react'

interface Props {
  cityName: string
}

export default function CityMapEmbed({ cityName }: Props) {
  const key = import.meta.env.VITE_GOOGLE_MAPS_EMBED_KEY as string | undefined
  const src = key
    ? `https://www.google.com/maps/embed/v1/place?key=${key}&q=${encodeURIComponent(cityName)}`
    : ''
  const hasKey = !!key
  const [isLoading, setIsLoading] = useState<boolean>(hasKey)

  useEffect(() => {
    // whenever city or key changes (src changes), show loader again
    setIsLoading(hasKey)
  }, [src, hasKey])

  return (
    <Card>
      <CardContent>
        {key ? (
          <div className="relative w-full h-64 md:h-80">
            {isLoading && (
              <div className="absolute inset-0 z-10 flex items-center justify-center"><Loading /></div>
            )}
            <iframe
              title={`Map of ${cityName}`}
              src={src}
              className="w-full h-full rounded-lg border-0"
              loading="lazy"
              referrerPolicy="no-referrer-when-downgrade"
              frameBorder={0}
              style={{ border: 0 }}
              onLoad={() => setIsLoading(false)}
            />
          </div>
        ) : (
          <div className="h-64 w-full flex items-center justify-center text-white/70">
            Set VITE_GOOGLE_MAPS_EMBED_KEY to display the map.
          </div>
        )}
      </CardContent>
    </Card>
  )
}
