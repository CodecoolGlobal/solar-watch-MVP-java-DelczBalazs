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
          <div className="text-sm text-white/70">Temperature</div>
          <div className="text-2xl font-semibold">{Math.round(weather.temperatureCelsius)}°C</div>
          <div className="text-sm text-white/70">Feels like {Math.round(weather.feelsLikeCelsius)}°C</div>
        </CardContent>
      </Card>

      <Card>
        <CardContent className="space-y-1">
          <div className="text-sm text-white/70">Pressure</div>
          <div className="text-2xl font-semibold">{weather.pressureHpa} hPa</div>
        </CardContent>
      </Card>

      <Card>
        <CardContent className="space-y-1">
          <div className="text-sm text-white/70">Humidity</div>
          <div className="text-2xl font-semibold">{weather.humidityPercent}%</div>
        </CardContent>
      </Card>

      <Card>
        <CardContent className="space-y-1">
          <div className="text-sm text-white/70">Wind</div>
          <div className="text-2xl font-semibold">{Math.round(weather.windSpeedKmh)} km/h</div>
          <div className="text-sm text-white/70">Direction {weather.windDirection}</div>
        </CardContent>
      </Card>
    </div>
  )
}
