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
        @QueryParam("active") active: Boolean?,
        @QueryParam("lat") lat: Float?,
        @QueryParam("lon") lon: Float?,
        @QueryParam("timeIntervalH") timeIntervalH: Int?
        ) {
        userService.addUser(name, active, lat,lon, timeIntervalH)
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
            (minNotify == null || user.timeIntervalH >= minNotify)
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
            request.timeIntervalH.let { user.timeIntervalH = it }

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
