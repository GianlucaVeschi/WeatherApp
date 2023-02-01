package com.gianlucaveschi.weatherapp.presentation.ui.weather

import com.gianlucaveschi.weatherapp.domain.model.WeatherInfo

data class WeatherState(
    val weatherInfo: WeatherInfo? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)