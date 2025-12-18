package org.acme.restClients

import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.core.Response
import org.eclipse.microprofile.rest.client.inject.RestClient

@ApplicationScoped
class WeatherService(
    @RestClient
    private val client: WeatherServiceClient
    ){

    fun createSubscription(
        id: Int,
        timeIntervalH: Int,
        lon: Float,
        lat: Float
    ): Response{
        return client.createSubscription(id, time = timeIntervalH, lon, lat)
    }
    fun updateSubscription(
        id: Int,
        timeIntervalH: Int,
        lon: Float,
        lat: Float,
        active: Boolean,
    ): Response{
        return client.updateSubscription(id, time = timeIntervalH, lon, lat, active)
    }
    fun deleteSubscription(
        id: Int
    ): Response{
        return client.deleteSubscription(id)
    }
}
