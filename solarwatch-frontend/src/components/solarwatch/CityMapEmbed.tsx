import { Card, CardContent } from '@/components/ui/card'

interface Props {
  cityName: string
}

export default function CityMapEmbed({ cityName }: Props) {
  const key = import.meta.env.VITE_GOOGLE_MAPS_EMBED_KEY as string | undefined
  const src = key
    ? `https://www.google.com/maps/embed/v1/place?key=${key}&q=${encodeURIComponent(cityName)}`
    : ''

  return (
    <Card>
      <CardContent>
        {key ? (
          <iframe
            title={`Map of ${cityName}`}
            src={src}
            className="w-full h-64 rounded-md border-0"
            loading="lazy"
            referrerPolicy="no-referrer-when-downgrade"
          />
        ) : (
          <div className="h-64 w-full flex items-center justify-center text-white/70">
            Set VITE_GOOGLE_MAPS_EMBED_KEY to display the map.
          </div>
        )}
      </CardContent>
    </Card>
  )
}
