package com.gianlucaveschi.weatherapp.data.model

import com.squareup.moshi.Json

/**
 * Contains information about all weather conditions for the next 7 days.
 * */
data class WeatherDataApiModel(
    val time: List<String>,
    @field:Json(name = "temperature_2m") val temperatures: List<Double>,
    @field:Json(name = "weathercode") val weatherCodes: List<Int>,
    @field:Json(name = "pressure_msl") val pressures: List<Double>,
    @field:Json(name = "windspeed_10m") val windSpeeds: List<Double>,
    @field:Json(name = "relativehumidity_2m") val humidities: List<Double>
)