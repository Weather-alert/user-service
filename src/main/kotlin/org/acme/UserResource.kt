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
import jakarta.ws.rs.core.Response

@Path("/users")
@RequestScoped
class UserResource {

    @Inject
    @field:Default
    lateinit var userService: UserService

    @POST
    fun createUser(
        @QueryParam("name") name: String,
        @QueryParam("active") active: Boolean = false,
        @QueryParam("lat") lat: Float = 0f,
        @QueryParam("lon") lon: Float = 0f,
        @QueryParam("notifyInterval") notifyInterval: Int = 1*60*100
        ) {
        userService.addUser(name, active, LatLon(lat,lon), notifyInterval)
    }

    @GET
    @Path("{id}")
    fun returnUser(@PathParam("id") id: Int): Response{
        val user = userService.getUser(id)

        return if(user == null){
            Response.status(Response.Status.NOT_FOUND)
              .entity("user $id not found")
              .build()
        } else{
            Response.ok(user).build()
        }
    }

    @GET
    @Path("")
    fun returnUsers(
        @QueryParam("id") id :Int?,
        @QueryParam("name") name: String?,
        @QueryParam("active") active: Boolean?,
        @QueryParam("minLat") minLat: Float?,
        @QueryParam("minLon") minLon: Float?,
        @QueryParam("maxLat") maxLat: Float?,
        @QueryParam("maxLon") maxLon: Float?,
        @QueryParam("minNotify") minNotify: Long?
    ): Map<Int,User> {
        return userService.getUsers().filterValues { user ->
            (id == null || user.id == id) &&
            (name == null || user.name == name) &&
            (active == null || user.active == active) &&
            (minLat == null  || user.latLon.lat >= minLat) &&
            (minLon == null  || user.latLon.lon >= minLon) &&
            (maxLat == null  || user.latLon.lat >= maxLat) &&
            (maxLon == null  || user.latLon.lon >= maxLon) &&
            (minNotify == null || user.notifyInterval >= minNotify)
        }
    }

    @PATCH
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    fun updateUser(
        @PathParam("id") id: Int,
        request: User
    ): Response{
        val user = userService.getUser(id)
        return if(user == null){
            Response.status(Response.Status.NOT_FOUND)
                .entity("user $id not found")
                .build()
        }else{
            request.active.let { user.active = it }
            request.latLon.let { user.latLon = it }
            request.notifyInterval.let { user.notifyInterval = it }

            userService.updateUser(user.id,user)

            Response.ok().build()
        }
    }

    @DELETE
    @Path("{id}")
    fun removeUser(@PathParam("id") id: Int ): Response{
        val user = userService.removeUser(id)
        return if(user == null){
            Response.status(Response.Status.NOT_FOUND)
                .entity("user $id not found")
                .build()
        }else{
            Response.ok().build()
        }
    }
}
