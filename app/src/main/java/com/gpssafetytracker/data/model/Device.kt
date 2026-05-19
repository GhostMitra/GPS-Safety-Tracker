package com.gpssafetytracker.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Device(
    @Json(name = "id") val id: String,
    @Json(name = "name") val name: String,
    @Json(name = "latitude") val latitude: Double,
    @Json(name = "longitude") val longitude: Double,
    @Json(name = "battery_level") val batteryLevel: Int,
    @Json(name = "signal_strength") val signalStrength: Int,
    @Json(name = "last_updated") val lastUpdated: Long,
    @Json(name = "status") val status: DeviceStatus
)

enum class DeviceStatus {
    @Json(name = "online") ONLINE,
    @Json(name = "offline") OFFLINE,
    @Json(name = "low_battery") LOW_BATTERY
}
