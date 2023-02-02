package com.gianlucaveschi.weatherapp.data.remote

import com.gianlucaveschi.weatherapp.data.util.NetworkService.WEATHER_ENDPOINT
import com.gianlucaveschi.weatherapp.data.weather.WeatherDataApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenMeteoWeatherApi {

    @GET(WEATHER_ENDPOINT)
    suspend fun getWeatherDataGenModel(
        @Query("latitude") lat: Double,
        @Query("longitude") long: Double
    ): WeatherDataApiResponse
}

