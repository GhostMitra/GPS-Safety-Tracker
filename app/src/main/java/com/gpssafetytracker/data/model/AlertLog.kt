package com.gpssafetytracker.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AlertLog(
    @Json(name = "id") val id: String,
    @Json(name = "timestamp") val timestamp: Long,
    @Json(name = "device_name") val deviceName: String,
    @Json(name = "geofence_name") val geofenceName: String,
    @Json(name = "type") val type: AlertType,
    @Json(name = "message") val message: String
)

enum class AlertType {
    @Json(name = "entry") ENTRY,
    @Json(name = "exit") EXIT
}
