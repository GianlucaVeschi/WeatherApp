package com.gianlucaveschi.weatherapp.domain.repo

import com.gianlucaveschi.weatherapp.domain.model.WeatherInfo
import com.gianlucaveschi.weatherapp.domain.util.Resource

interface WeatherRepository {
    suspend fun getWeatherDataGen(lat: Double, long: Double): Resource<WeatherInfo>
}