export interface Coordinates {
  lat: number
  lon: number
}

export interface WeatherDetails {
  temperatureCelsius: number
  feelsLikeCelsius: number
  uvIndex: number
  humidityPercent: number
  windSpeedKmh: number
  windDirection: string
}

export interface SolarWatchDashboard {
  city: string
  country: string
  state?: string | null
  coordinates: Coordinates
  date: string
  timezone: string
  sunrise: string
  sunset: string
  weather: WeatherDetails
}
