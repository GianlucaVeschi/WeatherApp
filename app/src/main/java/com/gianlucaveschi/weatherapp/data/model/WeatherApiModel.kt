package com.gianlucaveschi.weatherapp.data.model

import com.squareup.moshi.Json

data class WeatherApiModel(
    @field:Json(name = "hourly") val weatherData: WeatherDataApiModel
)