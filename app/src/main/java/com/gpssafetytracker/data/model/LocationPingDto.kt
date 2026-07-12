package com.gpssafetytracker.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LocationPingDto(
    @Json(name = "device_id") val deviceId: String,
    @Json(name = "received_at") val receivedAt: String,
    @Json(name = "timestamp_ms") val timestampMs: Long,
    @Json(name = "wifi_status") val wifiStatus: String,
    @Json(name = "wifi_ssid") val wifiSsid: String,
    @Json(name = "wifi_ip") val wifiIp: String,
    @Json(name = "wifi_rssi") val wifiRssi: Int,
    @Json(name = "gps_locked") val gpsLocked: Boolean,
    @Json(name = "satellites") val satellites: Int,
    @Json(name = "location_valid") val locationValid: Boolean,
    @Json(name = "lat") val lat: Double?,
    @Json(name = "lng") val lng: Double?
)
