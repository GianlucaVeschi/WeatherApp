package com.gianlucaveschi.weatherapp.domain.location

import android.location.Location

// Domain module should not contain android components
interface LocationTracker {
    suspend fun getCurrentLocation(): Location?
}