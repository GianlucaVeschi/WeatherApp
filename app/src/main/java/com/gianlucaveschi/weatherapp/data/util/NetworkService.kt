package com.gianlucaveschi.weatherapp.data.util

object NetworkService {
    const val BASE_URL: String = "https://api.open-meteo.com/"
    const val WEATHER_ENDPOINT = "v1/forecast?hourly=temperature_2m,weathercode,relativehumidity_2m,windspeed_10m,pressure_msl"
}