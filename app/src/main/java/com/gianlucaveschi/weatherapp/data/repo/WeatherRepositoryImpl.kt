package com.gianlucaveschi.weatherapp.data.repo

import com.gianlucaveschi.weatherapp.data.mapper.toWeatherInfo
import com.gianlucaveschi.weatherapp.data.remote.WeatherApi
import com.gianlucaveschi.weatherapp.domain.model.WeatherInfo
import com.gianlucaveschi.weatherapp.domain.repo.WeatherRepository
import com.gianlucaveschi.weatherapp.domain.util.Resource
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val api: WeatherApi
) : WeatherRepository {

    override suspend fun getWeatherDataGen(
        lat: Double, long: Double
    ): Resource<WeatherInfo> = try {
        Resource.Success(
            data = api.getWeatherDataGenModel(
                lat = lat,
                long = long
            ).toWeatherInfo()
        )
    } catch (e: Exception) {
        e.printStackTrace()
        Resource.Error(e.message ?: "An unknown error occurred.")
    }
}