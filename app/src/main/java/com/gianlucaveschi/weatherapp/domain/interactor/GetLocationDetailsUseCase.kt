package com.gianlucaveschi.weatherapp.domain.interactor

import android.location.Address
import android.location.Geocoder
import com.gianlucaveschi.weatherapp.domain.repo.WeatherRepository
import com.gianlucaveschi.weatherapp.domain.util.Resource
import com.gianlucaveschi.weatherapp.domain.weather.WeatherInfo
import javax.inject.Inject

class GetLocationDetailsUseCase @Inject constructor(
    private val geocoder: Geocoder,
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(query: String): Resource<WeatherInfo> {
        val location: List<Address> = geocoder.getFromLocationName(query, 1)
        return try {
            if (location.isNotEmpty()) {
                location[0].let {
                    if (it.hasLatitude() && it.hasLongitude()) {
                        repository.getWeatherData(it.latitude, it.longitude)
                    } else {
                        Resource.Error("error, could not retrieve longitude and latitude")
                    }
                }
            } else {
                Resource.Error("error, location is unknown")
            }
        } catch (exception : Exception){
            Resource.Error("error: $exception")
        }
    }
}