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

    private val userMap = mutableMapOf<String, User>()

    var seqNum = 0

    fun createUser(id: String, active: Boolean?, lat: Float?, lon: Float?, timeIntervalH: Int?): Response? {
        val finalLat = lat ?: 0f
        val finalLon = lon ?: 0f
        val latLon = LatLon(finalLat,finalLon)
        val finalTimeIntervalH = timeIntervalH ?: 5

        if(userMap.get(id) != null){
            // user already created
            return null
        }

        userMap[id] = User(
            seqNum,
            id,
            active ?: false,
            latLon,
            finalTimeIntervalH
            )
        seqNum++

        val r = weatherServiceClient.createSubscription(id, timeIntervalH = finalTimeIntervalH, lon = latLon.lon, lat = latLon.lat)
        return r
    }
    fun getUser(id: String): User? = userMap[id]

    fun getUsers(): Map<String, User> = userMap

    fun updateUser(id: String, user: User): Response {
        userMap[id] = user
        return weatherServiceClient.updateSubscription(user.id, user.timeIntervalH, user.latLon.lon, user.latLon.lat, user.active)
    }
    fun removeUser(id: String): Response?{
        if (userMap.remove(id) == null){
            return null
        }
        val r = weatherServiceClient.deleteSubscription(id)
        return r
    }

}