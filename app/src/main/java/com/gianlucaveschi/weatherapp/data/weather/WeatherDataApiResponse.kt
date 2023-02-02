package com.gianlucaveschi.weatherapp.data.weather

data class WeatherDataApiResponse(
    val elevation: Double,
    val generationtime_ms: Double,
    val hourly: HourlyWeatherData,
    val hourly_units: HourlyWeatherUnits,
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    val timezone_abbreviation: String,
    val utc_offset_seconds: Int
)