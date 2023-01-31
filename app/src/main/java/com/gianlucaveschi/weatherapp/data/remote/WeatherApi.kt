package com.gianlucaveschi.weatherapp.data.remote

import com.gianlucaveschi.weatherapp.data.model.WeatherApiModel
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET(WEATHER_ENDPOINT)
    suspend fun getWeatherData(
        @Query("latitude") lat: Double,
        @Query("longitude") long: Double
    ): WeatherApiModel

    companion object {
        const val WEATHER_ENDPOINT =
            "v1/forecast?hourly=temperature_2m,weathercode,relativehumidity_2m,windspeed_10m,pressure_msl"
    }
}

