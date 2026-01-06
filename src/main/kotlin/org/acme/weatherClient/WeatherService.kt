package org.acme.weatherClient

import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.core.Response
import org.eclipse.microprofile.rest.client.inject.RestClient
import org.jboss.resteasy.reactive.ClientWebApplicationException

@ApplicationScoped
class WeatherService(
    @RestClient
    private val client: WeatherServiceClient
    ){

    fun createSubscription(
        id: String,
        timeIntervalH: Int,
        lon: Float,
        lat: Float,
        active: Boolean
    ): Boolean{
        return try {
            val r = client.createSubscription(id, time = timeIntervalH, lon, lat, active)
            r.status == Response.Status.OK.statusCode
        } catch(e: Exception) {
            return false
        }
    }
    fun updateSubscription(
        id: String,
        timeIntervalH: Int,
        lon: Float,
        lat: Float,
        active: Boolean,
    ): Boolean {
        return try {
            val r = client.updateSubscription(id, time = timeIntervalH, lon, lat, active)
            r.status == Response.Status.OK.statusCode
        } catch(e: Exception) {
            return false
        }
    }
    fun deleteSubscription(
        id: String
    ): Boolean {
        return try {
            val r = client.deleteSubscription(id = id)
            r.status == Response.Status.OK.statusCode
        } catch(e: Exception) {
            return false
        }
    }
}
