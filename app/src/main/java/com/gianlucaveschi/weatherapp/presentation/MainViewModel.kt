package com.gianlucaveschi.weatherapp.presentation

import android.location.Address
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gianlucaveschi.weatherapp.domain.location.LocationTracker
import com.gianlucaveschi.weatherapp.domain.repo.WeatherRepository
import com.gianlucaveschi.weatherapp.domain.util.Resource
import com.gianlucaveschi.weatherapp.presentation.ui.weather.WeatherState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: WeatherRepository,
    private val locationTracker: LocationTracker
) : ViewModel() {

    private val _state = MutableStateFlow<WeatherState>(WeatherState(isLoading = true))
    val state = _state.asStateFlow()

    fun loadWeatherInfo() {
        viewModelScope.launch {
            _state.value = _state.value.copy(
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
                                _state.value = _state.value.copy(
                                    location = cityName,
                                    weatherInfo = result.data,
                                    isLoading = false,
                                    error = null
                                )
                            }
                            is Resource.Error -> {
                                _state.value = _state.value.copy(
                                    weatherInfo = null,
                                    isLoading = false,
                                    error = result.message
                                )
                            }
                        }
                    } ?: run {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            error = "Couldn't retrieve location. Make sure to grant permission and enable GPS."
                        )
                    }
                }
                is Resource.Error -> {
                    _state.value = _state.value.copy(
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