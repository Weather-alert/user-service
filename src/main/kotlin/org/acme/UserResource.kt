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

@Path("/api/v1/users")
@RequestScoped
class UserResource {

    @Inject
    @field:Default
    private lateinit var userService: UserService

    @GET
    @Path("/hello/hello")
    fun hello():Response{
        return Response.ok("hello you just compiled me with CI/CD").build()
    }
    @POST
    fun createUser(
        @QueryParam("id") id: String,
        @QueryParam("active") active: Boolean?,
        @QueryParam("lat") lat: Float?,
        @QueryParam("lon") lon: Float?,
        @QueryParam("timeIntervalH") timeIntervalH: Int?,
        @QueryParam("fcmToken") token: String?,
        ): Response {
        println("got token: $token")
        try {
            val r = userService.createUser(id, active, lat, lon, timeIntervalH, token)

            return if(r == null){
                    Response.status(Response.Status.CONFLICT)
                        .entity("user with $id already created")
                        .build()
                }else if(r == false) {
                    Response.status(Response.Status.NOT_ACCEPTABLE)
                        .entity("Failed to communicate with either notification service and weather service")
                        .build()
                }else{
                    Response.ok().build()
                }
        } catch(ex: RuntimeException) {
            return Response.status(Response.Status.CONFLICT)
                .entity(ex.message)
                .build()
        }
    }

    @GET
    @Path("{id}")
    fun returnUser(@PathParam("id") id: String): Response{
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
        @QueryParam("seqNum") seqNum :Int?,
        @QueryParam("id") id: String?,
        @QueryParam("active") active: Boolean?,
        @QueryParam("minLat") minLat: Float?,
        @QueryParam("minLon") minLon: Float?,
        @QueryParam("maxLat") maxLat: Float?,
        @QueryParam("maxLon") maxLon: Float?,
        @QueryParam("minNotify") minNotify: Long?
    ): List<User> {
        return userService.getUsers()
            /*.filterValues { user ->
            (seqNum == null || user.seqNum == seqNum) &&
            (id == null || user.id == id) &&
            (active == null || user.active == active) &&
            (minLat == null  || user.latLon.lat >= minLat) &&
            (minLon == null  || user.latLon.lon >= minLon) &&
            (maxLat == null  || user.latLon.lat >= maxLat) &&
            (maxLon == null  || user.latLon.lon >= maxLon) &&
            (minNotify == null || user.timeIntervalH >= minNotify)
        }
        */
    }

    @PATCH
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    fun updateUser(
        @PathParam("id") id: String,
        request: UserUpdateRequest
    ): Response{
        val user = userService.getUser(id)
        return if(user == null){
            Response.status(Response.Status.NOT_FOUND)
                .entity("user $id not found")
                .build()
        }else{
            request.active?.let { user.active = it }
            request.latitude?.let { lat->
                request.longitude?.let { lon->
                    user.latLon = LatLon(lat,lon)
                }
            }
            request.timeIntervalH?.let { user.timeIntervalH = it }

            if(userService.updateUser(user.id,user) != true) {
                Response.status(Response.Status.NOT_ACCEPTABLE)
                    .entity("Failed to communicate with either notification service and weather service")
                    .build()
            }
            else
                Response.ok().build()
        }
    }

    @DELETE
    @Path("{id}")
    fun removeUser(@PathParam("id") id: String ): Response{
        val user = userService.removeUser(id)
        return if(user == null){
            Response.status(Response.Status.NOT_FOUND)
                .entity("user $id not found")
                .build()
        }else{
            if(user == false){
                Response.status(Response.Status.NOT_ACCEPTABLE)
                    .entity("Failed to communicate with either notification service and weather service")
                    .build()
            }
            Response.ok().build()
        }
    }
}
