package com.gianlucaveschi.weatherapp.presentation.ui.weather

import com.gianlucaveschi.weatherapp.domain.weather.WeatherInfo

data class WeatherState(
    val location : String = "",
    val weatherInfo: WeatherInfo? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)