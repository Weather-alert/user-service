package org.acme

import jakarta.enterprise.context.RequestScoped
import jakarta.enterprise.inject.Default
import jakarta.inject.Inject
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.DELETE
import jakarta.ws.rs.GET
import jakarta.ws.rs.PATCH
import jakarta.ws.rs.POST
import jakarta.ws.rs.PUT
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.QueryParam
import jakarta.ws.rs.core.MediaType

@Path("/users")
@RequestScoped
class UserResource {

    @Inject
    @field:Default
    lateinit var userRepo: UserRepo

    @POST
    fun createUser(
        @QueryParam("name") name: String,
        @QueryParam("active") active: Boolean = false,
        @QueryParam("lat") lat: Float = 0f,
        @QueryParam("lng") lng: Float = 0f,
        @QueryParam("notifyInterval") notifyInterval: Long = 1*60*100
        ) {
        userRepo.addUser(name, active, LatLng(lat,lng), notifyInterval)
    }

    @GET
    @Path("{id}")
    fun returnUser(@PathParam("id") id: Int) = userRepo.getUser(id)

    @GET
    @Path("")
    fun returnUsers(
        @QueryParam("id") id :Int?,
        @QueryParam("name") name: String?,
        @QueryParam("active") active: Boolean?,
        @QueryParam("minLat") minLat: Float?,
        @QueryParam("minLng") minLng: Float?,
        @QueryParam("maxLat") maxLat: Float?,
        @QueryParam("maxLng") maxLng: Float?,
        @QueryParam("minNotify") minNotify: Long?
    ): Map<Int,User> {
        return userRepo.getUsers().filterValues { user ->
            (id == null || user.id == id) &&
            (name == null || user.name == name) &&
            (active == null || user.active == active) &&
            (minLat == null  || user.latLng.lat >= minLat) &&
            (minLng == null  || user.latLng.lng >= minLng) &&
            (maxLat == null  || user.latLng.lat >= maxLat) &&
            (maxLng == null  || user.latLng.lng >= maxLng) &&
            (minNotify == null || user.notifyInterval >= minNotify)
        }
    }

    @PATCH
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    fun updateUser(
        @PathParam("id") id: Int,
        request: User
    ){
        val user = userRepo.getUser(id) ?: return
        request.active.let { user.active = it }
        request.latLng.let { user.latLng = it }
        request.notifyInterval.let { user.notifyInterval = it }
        userRepo.updateUser(user.id,user)
    }

    @DELETE
    @Path("{id}")
    fun removeUser(@PathParam("id") id: Int ){
        userRepo.removeUser(id)

    }
}
