package com.gpssafetytracker.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Geofence(
    @Json(name = "id") val id: String,
    @Json(name = "name") val name: String,
    @Json(name = "latitude") val latitude: Double,
    @Json(name = "longitude") val longitude: Double,
    @Json(name = "radius") val radius: Double, // in meters
    @Json(name = "is_active") val isActive: Boolean
)
