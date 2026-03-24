package com.sunsafe.app.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import com.google.android.gms.location.*
import com.sunsafe.app.domain.repository.LocationRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeout
import timber.log.Timber
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@Singleton
class LocationRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : LocationRepository {

    private val fusedLocationClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }

    @SuppressLint("MissingPermission")
    override suspend fun getCurrentLocation(): Result<Pair<Double, Double>> {
        return try {
            withTimeout(15_000L) {
                suspendCancellableCoroutine { cont ->
                    val locationRequest = CurrentLocationRequest.Builder()
                        .setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY)
                        .setMaxUpdateAgeMillis(60_000L)
                        .build()

                    fusedLocationClient.getCurrentLocation(locationRequest, null)
                        .addOnSuccessListener { location ->
                            if (location != null) {
                                cont.resume(Result.success(Pair(location.latitude, location.longitude)))
                            } else {
                                // Fallback: get last known location
                                fusedLocationClient.lastLocation.addOnSuccessListener { lastLocation ->
                                    if (lastLocation != null) {
                                        cont.resume(Result.success(Pair(lastLocation.latitude, lastLocation.longitude)))
                                    } else {
                                        cont.resume(Result.failure(Exception("Location unavailable")))
                                    }
                                }.addOnFailureListener { e ->
                                    cont.resume(Result.failure(e))
                                }
                            }
                        }
                        .addOnFailureListener { e ->
                            cont.resume(Result.failure(e))
                        }
                }
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to get location")
            Result.failure(e)
        }
    }

    override suspend fun getLocationName(lat: Double, lon: Double): String {
        return try {
            val geocoder = Geocoder(context, Locale.getDefault())
            @Suppress("DEPRECATION")
            val addresses = geocoder.getFromLocation(lat, lon, 1)
            if (!addresses.isNullOrEmpty()) {
                val address = addresses[0]
                buildString {
                    address.locality?.let { append(it) }
                    address.adminArea?.let {
                        if (isNotEmpty()) append(", ")
                        append(it)
                    }
                    if (isEmpty()) append(address.countryName ?: "Unknown")
                }
            } else {
                "%.2f, %.2f".format(lat, lon)
            }
        } catch (e: Exception) {
            Timber.e(e, "Geocoding failed")
            "%.2f, %.2f".format(lat, lon)
        }
    }
}
