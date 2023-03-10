package com.gianlucaveschi.weatherapp.data.location

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.util.Log
import androidx.core.content.ContextCompat
import com.gianlucaveschi.weatherapp.domain.location.LocationTracker
import com.gianlucaveschi.weatherapp.domain.util.Resource
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

@ExperimentalCoroutinesApi
class DefaultLocationTracker @Inject constructor(
    private val locationClient: FusedLocationProviderClient,
    private val application: Application,
    private val geocoder: Geocoder
) : LocationTracker {

    override suspend fun getCurrentLocation(): Resource<Location?> {
        val hasAccessFineLocationPermission = ContextCompat.checkSelfPermission(
            application,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val hasAccessCoarseLocationPermission = ContextCompat.checkSelfPermission(
            application,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val locationManager =
            application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (!hasAccessCoarseLocationPermission || !hasAccessFineLocationPermission || !isGpsEnabled) {
            return Resource.Error("Could not get location")
        }

        // Convert callback into cancellable coroutine
        return suspendCancellableCoroutine { cont ->
            locationClient.lastLocation.apply {
                if (isComplete) {
                    if (isSuccessful) {
                        cont.resume(Resource.Success(result))
                    } else {
                        cont.resume(Resource.Error("Couldn't get location"))
                    }
                    return@suspendCancellableCoroutine
                }
                addOnSuccessListener {
                    cont.resume(Resource.Success(result))
                }
                addOnFailureListener {
                    cont.resume(Resource.Error("Couldn't get location ${it.message}"))
                }
                addOnCanceledListener {
                    cont.cancel()
                }
            }
        }
    }

    override fun getLocationAddress(lat: Double, long: Double): List<Address> {
        val addresses: List<Address> = geocoder.getFromLocation(lat, long, 1)
        return if (addresses.isNotEmpty()) {
            Log.d("DefaultLocationTracker", "getCityNameWithLocation: ${addresses[0]}")
            addresses
        } else listOf()
    }
}