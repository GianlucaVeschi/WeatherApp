package com.gianlucaveschi.weatherapp.presentation

import android.app.Application
import android.location.Address
import android.location.Geocoder
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gianlucaveschi.weatherapp.domain.location.LocationTracker
import com.gianlucaveschi.weatherapp.domain.repo.WeatherRepository
import com.gianlucaveschi.weatherapp.domain.util.Resource
import com.gianlucaveschi.weatherapp.presentation.ui.weather.WeatherState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: WeatherRepository,
    private val locationTracker: LocationTracker
) : ViewModel() {

    // What is by?
    // Why private set?
    var state by mutableStateOf(WeatherState())
        private set

    // What is the difference?
    // private val _state = mutableStateOf(WeatherState())
    // val state2 = _state

    fun loadWeatherInfo() {
        viewModelScope.launch {
            state = state.copy(
                isLoading = true,
                error = null
            )
            locationTracker.getCurrentLocation()?.let { location ->
                val result = location.run {
                    repository.getWeatherData(latitude, longitude)
                }
                val addresses = location.run {
                    locationTracker.getLocationAddress(latitude, longitude)
                }
                val cityName = if (addresses.isNotEmpty()) addresses[0].locality else ""
                when (result) {
                    is Resource.Success -> {
                        state = state.copy(
                            location = cityName,
                            weatherInfo = result.data,
                            isLoading = false,
                            error = null
                        )
                    }
                    is Resource.Error -> {
                        state = state.copy(
                            weatherInfo = null,
                            isLoading = false,
                            error = result.message
                        )
                    }
                }
            } ?: run {
                state = state.copy(
                    isLoading = false,
                    error = "Couldn't retrieve location. Make sure to grant permission and enable GPS."
                )
            }
        }
    }
}