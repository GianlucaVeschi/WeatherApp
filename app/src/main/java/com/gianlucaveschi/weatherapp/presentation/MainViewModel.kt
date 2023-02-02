package com.gianlucaveschi.weatherapp.presentation

import android.location.Address
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gianlucaveschi.weatherapp.domain.location.LocationTracker
import com.gianlucaveschi.weatherapp.domain.repo.WeatherRepository
import com.gianlucaveschi.weatherapp.domain.util.Resource
import com.gianlucaveschi.weatherapp.presentation.ui.weather.WeatherState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
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
            when (val locationResource = locationTracker.getCurrentLocation()) {
                is Resource.Success -> {
                    locationResource.data?.run {
                        val result = repository.getWeatherDataGen(latitude, longitude)
                        val addresses = locationTracker.getLocationAddress(latitude, longitude)
                        val cityName = getCityName(addresses)
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
                is Resource.Error -> {
                    state = state.copy(
                        weatherInfo = null,
                        isLoading = false,
                        error = locationResource.message
                    )
                }
            }
        }
    }

    private fun getCityName(addresses: List<Address>): String =
        if (addresses.isNotEmpty()) "${addresses[0].locality}, ${addresses[0].countryName}" else ""
}