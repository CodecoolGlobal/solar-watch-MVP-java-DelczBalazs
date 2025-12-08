import { Card, CardContent } from '@/components/ui/card'
import type { WeatherDetails } from '@/types/solarwatch'

interface Props {
  weather: WeatherDetails
}

export default function WeatherSummaryCards({ weather }: Props) {
  return (
    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
      <Card>
        <CardContent className="space-y-1">
          <div className="text-sm text-white/70 flex items-center gap-2">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round" className="w-4 h-4">
              <path d="M14 14.76V5a2 2 0 0 0-4 0v9.76" />
              <path d="M8 15a4 4 0 1 0 8 0" />
            </svg>
            Temperature
          </div>
          <div className="text-2xl font-semibold">{Math.round(weather.temperatureCelsius)}°C</div>
          <div className="text-sm text-white/70">Feels like {Math.round(weather.feelsLikeCelsius)}°C</div>
        </CardContent>
      </Card>

      <Card>
        <CardContent className="space-y-1">
          <div className="text-sm text-white/70 flex items-center gap-2">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round" className="w-4 h-4">
              <path d="M4 13a8 8 0 1 1 16 0" />
              <path d="M12 13l3-3" />
            </svg>
            Pressure
          </div>
          <div className="text-2xl font-semibold">{weather.pressureHpa} hPa</div>
        </CardContent>
      </Card>

      <Card>
        <CardContent className="space-y-1">
          <div className="text-sm text-white/70 flex items-center gap-2">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round" className="w-4 h-4">
              <path d="M12 22a7 7 0 0 1-7-7c0-4 7-11 7-11s7 7 7 11a7 7 0 0 1-7 7z" />
            </svg>
            Humidity
          </div>
          <div className="text-2xl font-semibold">{weather.humidityPercent}%</div>
        </CardContent>
      </Card>

      <Card>
        <CardContent className="space-y-1">
          <div className="text-sm text-white/70 flex items-center gap-2">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round" className="w-4 h-4">
              <path d="M3 12h12a3 3 0 1 0-3-3" />
              <path d="M3 18h9a3 3 0 1 1-3 3" />
            </svg>
            Wind
          </div>
          <div className="text-2xl font-semibold">{Math.round(weather.windSpeedKmh)} km/h</div>
          <div className="text-sm text-white/70">Direction {weather.windDirection}</div>
        </CardContent>
      </Card>
    </div>
  )
}
