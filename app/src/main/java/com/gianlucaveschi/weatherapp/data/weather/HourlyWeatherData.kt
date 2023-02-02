package com.gianlucaveschi.weatherapp.data.weather

data class HourlyWeatherData(
    val pressure_msl: List<Double>,
    val relativehumidity_2m: List<Double>,
    val temperature_2m: List<Double>,
    val time: List<String>,
    val weathercode: List<Int>,
    val windspeed_10m: List<Double>
)