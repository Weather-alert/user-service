package org.acme.notificationClient

import jakarta.enterprise.inject.Default
import jakarta.inject.Inject
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.DELETE
import jakarta.ws.rs.GET
import jakarta.ws.rs.PATCH
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.QueryParam
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient

@RegisterRestClient(baseUri = "http://localhost:8084/api/v1/notification")
interface NotificationServiceClient {

    @POST
    @Path("/users/{id}")
    fun createToken(
        @PathParam("id") userId: String,
        @QueryParam("token") token: String,
    ): Response

    @GET
    @Path("/users/{id}")
    fun readToken(
        @PathParam("id") userId: String,
    ): Response

    @GET
    @Path("/users")
    fun readUsers(): Response

    @PATCH
    @Path("/users/{id}")
    fun updateToken(
        @PathParam("id") userId: String,
        @QueryParam("token") token: String,
    ): Response

    @DELETE
    @Path("/users/{id}")
    fun deleteToken(
        @PathParam("id") userId: String
    ): Response

}