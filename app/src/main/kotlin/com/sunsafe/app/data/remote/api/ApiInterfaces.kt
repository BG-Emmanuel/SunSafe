package com.sunsafe.app.data.remote.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

// ─────────────────────────────────────────────────────────────────────────────
// OpenUV API
// ─────────────────────────────────────────────────────────────────────────────
interface OpenUvApi {
    @GET("uv")
    suspend fun getCurrentUv(
        @Header("x-access-token") apiKey: String,
        @Query("lat") lat: Double,
        @Query("lng") lng: Double,
        @Query("alt") altitude: Int = 0
    ): Response<OpenUvResponse>

    @GET("forecast")
    suspend fun getForecast(
        @Header("x-access-token") apiKey: String,
        @Query("lat") lat: Double,
        @Query("lng") lng: Double
    ): Response<OpenUvForecastResponse>
}

@JsonClass(generateAdapter = true)
data class OpenUvResponse(
    @Json(name = "result") val result: OpenUvResult
)

@JsonClass(generateAdapter = true)
data class OpenUvResult(
    @Json(name = "uv") val uv: Double,
    @Json(name = "uv_max") val uvMax: Double,
    @Json(name = "uv_max_time") val uvMaxTime: String?,
    @Json(name = "ozone") val ozone: Double?,
    @Json(name = "sun_info") val sunInfo: SunInfo?
)

@JsonClass(generateAdapter = true)
data class SunInfo(
    @Json(name = "sun_times") val sunTimes: SunTimes?
)

@JsonClass(generateAdapter = true)
data class SunTimes(
    @Json(name = "sunrise") val sunrise: String?,
    @Json(name = "sunset") val sunset: String?
)

@JsonClass(generateAdapter = true)
data class OpenUvForecastResponse(
    @Json(name = "result") val result: List<ForecastItem>
)

@JsonClass(generateAdapter = true)
data class ForecastItem(
    @Json(name = "uv") val uv: Double,
    @Json(name = "uv_max") val uvMax: Double,
    @Json(name = "uv_time") val uvTime: String,
    @Json(name = "uv_max_time") val uvMaxTime: String
)

// ─────────────────────────────────────────────────────────────────────────────
// OpenWeatherMap UV API (fallback)
// ─────────────────────────────────────────────────────────────────────────────
interface OpenWeatherApi {
    @GET("uvi")
    suspend fun getUvIndex(
        @Query("appid") apiKey: String,
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ): Response<OwmUvResponse>

    @GET("uvi/forecast")
    suspend fun getUvForecast(
        @Query("appid") apiKey: String,
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("cnt") count: Int = 5
    ): Response<List<OwmUvResponse>>
}

@JsonClass(generateAdapter = true)
data class OwmUvResponse(
    @Json(name = "lat") val lat: Double,
    @Json(name = "lon") val lon: Double,
    @Json(name = "date_iso") val dateIso: String,
    @Json(name = "date") val date: Long,
    @Json(name = "value") val value: Double
)
