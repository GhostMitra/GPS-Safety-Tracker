package com.gpssafetytracker.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SOSContact(
    @Json(name = "id") val id: String,
    @Json(name = "name") val name: String,
    @Json(name = "phone_number") val phoneNumber: String,
    @Json(name = "priority") val priority: Int
)
