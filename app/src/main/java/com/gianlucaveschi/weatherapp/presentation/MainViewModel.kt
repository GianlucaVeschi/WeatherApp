package com.gianlucaveschi.weatherapp.presentation

import android.location.Address
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gianlucaveschi.weatherapp.domain.interactor.GetLocationDetailsUseCase
import com.gianlucaveschi.weatherapp.domain.location.LocationTracker
import com.gianlucaveschi.weatherapp.domain.repo.WeatherRepository
import com.gianlucaveschi.weatherapp.domain.util.Resource
import com.gianlucaveschi.weatherapp.presentation.ui.components.SearchState
import com.gianlucaveschi.weatherapp.presentation.ui.weather.WeatherState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: WeatherRepository,
    private val getLocationDetailsUseCase: GetLocationDetailsUseCase,
    private val locationTracker: LocationTracker
) : ViewModel() {

    private var _searchBarState = mutableStateOf(SearchState.Closed)
    val searchBarState: State<SearchState> = _searchBarState

    private var _searchBarText = mutableStateOf("")
    val searchBarText: State<String> = _searchBarText

    private val _state = MutableStateFlow<WeatherState>(WeatherState(isLoading = true))
    val state = _state.asStateFlow()

    fun loadLocalWeatherInfo() {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = true,
                error = null
            )
            when (val locationResource = locationTracker.getCurrentLocation()) {
                is Resource.Success -> {
                    locationResource.data?.run {
                        val result = repository.getWeatherData(latitude, longitude)
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

    fun onTextChanged(newText: String) {
        _searchBarText.value = newText
    }

    fun onEvent(state: SearchState) {
        _searchBarState.value = state
    }

    fun onSearchClicked(query: String) {
        // Search weather for given country
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = true,
                error = null
            )
            when (val response = getLocationDetailsUseCase(query)) {
                is Resource.Success -> {
                    response.data?.run {
                        _state.value = _state.value.copy(
                            location = query,
                            weatherInfo = this,
                            isLoading = false,
                            error = null
                        )
                    }
                }
                is Resource.Error -> {
                    _state.value = _state.value.copy(
                        weatherInfo = null,
                        isLoading = false,
                        error = response.message
                    )
                }
            }
        }
    }

    private fun getCityName(addresses: List<Address>): String =
        if (addresses.isNotEmpty()) "${addresses[0].locality}, ${addresses[0].countryName}" else ""
}