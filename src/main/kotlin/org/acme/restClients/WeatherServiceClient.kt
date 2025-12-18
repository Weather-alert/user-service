package org.acme.restClients

import jakarta.ws.rs.DELETE
import jakarta.ws.rs.PATCH
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.QueryParam
import jakarta.ws.rs.core.Response
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient

@RegisterRestClient(baseUri = "http://localhost:8082/api/v1/weather")
interface WeatherServiceClient {
    @POST
    @Path("/subscription/{id}")
    fun createSubscription(
        @PathParam("id") id: Int,
        @QueryParam("timeIntervalH") time: Int,
        @QueryParam("lon") lon: Float,
        @QueryParam("lat") lat: Float,
    ): Response

    @PATCH
    @Path("/subscription/{id}")
    fun updateSubscription(
        @PathParam("id") id: Int,
        @QueryParam("timeIntervalH") time: Int,
        @QueryParam("lon") lon: Float,
        @QueryParam("lat") lat: Float,
        @QueryParam("active") active: Boolean,
    ): Response

    @DELETE
    @Path("/subscription/{id}")
    fun deleteSubscription(@PathParam("id") id: Int): Response
}