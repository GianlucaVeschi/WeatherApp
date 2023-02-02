package com.gianlucaveschi.weatherapp.domain.location

import android.location.Address
import android.location.Location
import com.gianlucaveschi.weatherapp.domain.util.Resource

// Domain module should not contain android components
interface LocationTracker {
    suspend fun getCurrentLocation(): Resource<Location?>
    fun getLocationAddress(lat: Double, long: Double): List<Address>
}