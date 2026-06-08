package com.gpssafetytracker.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Device(
    @Json(name = "id") val id: String,
    @Json(name = "name") val name: String,
    @Json(name = "type") val type: DeviceType = DeviceType.TRACKER,
    @Json(name = "latitude") val latitude: Double,
    @Json(name = "longitude") val longitude: Double,
    @Json(name = "speed") val speed: Double = 0.0,
    @Json(name = "battery_level") val batteryLevel: Int,
    @Json(name = "signal_strength") val signalStrength: Int,
    @Json(name = "last_updated") val lastUpdated: Long,
    @Json(name = "status") val status: DeviceStatus,
    @Json(name = "avatar") val avatar: String = "https://images.unsplash.com/photo-1533473359331-0135ef1b58bf?auto=format&fit=crop&q=80&w=120"
)

enum class DeviceType {
    @Json(name = "watch") WATCH,
    @Json(name = "collar") COLLAR,
    @Json(name = "tracker") TRACKER,
    @Json(name = "phone") PHONE
}

enum class DeviceStatus {
    @Json(name = "online") ONLINE,
    @Json(name = "offline") OFFLINE,
    @Json(name = "low_battery") LOW_BATTERY
}
