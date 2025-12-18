package org.acme

import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.Default
import jakarta.inject.Inject
import org.acme.restClients.WeatherService

@ApplicationScoped
class UserService {
    @Inject
    @field:Default
    private lateinit var weatherServiceClient: WeatherService

    private val userMap = mutableMapOf<Int, User>()

    var idSeq = 0

    fun addUser(name: String, active: Boolean, latLon: LatLon, notifyInterval: Int){
        userMap.put(idSeq, User(idSeq, name, active, latLon, notifyInterval))
        weatherServiceClient.createSubscription(idSeq, timeIntervalH = notifyInterval, lon = latLon.lon, lat = latLon.lat)

        idSeq++
    }
    fun getUser(id: Int): User? = userMap[id]

    fun getUsers(): Map<Int, User> = userMap

    fun updateUser(id: Int, user: User) {
        userMap[id] = user
        weatherServiceClient.updateSubscription(user.id, user.notifyInterval, user.latLon.lon, user.latLon.lat, user.active)
    }
    fun removeUser(id: Int){
        userMap.remove(id)
        weatherServiceClient.deleteSubscription(id)
    }

}