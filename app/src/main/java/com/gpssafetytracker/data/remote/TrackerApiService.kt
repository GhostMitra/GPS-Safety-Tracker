package com.gpssafetytracker.data.remote

import com.gpssafetytracker.data.model.Device
import com.gpssafetytracker.data.model.Geofence
import com.gpssafetytracker.data.model.SOSContact
import retrofit2.http.GET
import retrofit2.http.Path

interface TrackerApiService {
    @GET("devices")
    suspend fun getDevices(): List<Device>

    @GET("devices/{id}")
    suspend fun getDeviceById(@Path("id") id: String): Device

    @GET("geofences")
    suspend fun getGeofences(): List<Geofence>

    @GET("sos-contacts")
    suspend fun getSOSContacts(): List<SOSContact>
}
