package com.gianlucaveschi.weatherapp.domain.location

import android.location.Address
import android.location.Location

// Domain module should not contain android components
interface LocationTracker {
    suspend fun getCurrentLocation(): Location?
    fun getLocationAddress(lat: Double, long: Double): List<Address>
}