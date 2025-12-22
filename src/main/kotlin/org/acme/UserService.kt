package org.acme

import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.Default
import jakarta.inject.Inject
import jakarta.ws.rs.core.Response
import org.acme.restClients.WeatherService

@ApplicationScoped
class UserService {
    @Inject
    @field:Default
    private lateinit var weatherServiceClient: WeatherService

    private val userMap = mutableMapOf<Int, User>()

    var idSeq = 0

    fun addUser(name: String?, active: Boolean?, lat: Float?, lon: Float?, timeIntervalH: Int?){
        val finalLat = lat ?: 0f
        val finalLon = lon ?: 0f
        val latLon = LatLon(finalLat,finalLon)
        val finalTimeIntervalH = timeIntervalH ?: 5

        userMap.put(idSeq, User(idSeq, name ?: "null", active ?: false, latLon, finalTimeIntervalH))

        var r = weatherServiceClient.createSubscription(idSeq, timeIntervalH = finalTimeIntervalH, lon = latLon.lon, lat = latLon.lat)

        if(r.status != Response.Status.OK.statusCode){
            print("Error ${r.entity}")
        }

        idSeq++
    }
    fun getUser(id: Int): User? = userMap[id]

    fun getUsers(): Map<Int, User> = userMap

    fun updateUser(id: Int, user: User) {
        userMap[id] = user
        weatherServiceClient.updateSubscription(user.id, user.timeIntervalH, user.latLon.lon, user.latLon.lat, user.active)
    }
    fun removeUser(id: Int){
        userMap.remove(id)
        weatherServiceClient.deleteSubscription(id)
    }

}