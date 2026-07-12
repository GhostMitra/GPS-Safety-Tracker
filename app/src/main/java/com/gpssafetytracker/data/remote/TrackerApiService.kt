package com.gpssafetytracker.data.remote

import com.gpssafetytracker.data.model.LocationPingDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TrackerApiService {
    @GET("api/devices/{deviceId}/latest")
    suspend fun getLatest(
        @Path("deviceId") deviceId: String
    ): LocationPingDto

    @GET("api/devices/{deviceId}/history")
    suspend fun getHistory(
        @Path("deviceId") deviceId: String,
        @Query("limit") limit: Int = 50
    ): List<LocationPingDto>
}
