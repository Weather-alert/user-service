package org.acme

import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.context.RequestScoped
import jakarta.enterprise.inject.Default
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import jakarta.ws.rs.core.Response
import org.acme.notificationClient.NotificationService
import org.acme.weatherClient.WeatherService

@RequestScoped
class UserService {
    @Inject
    @field:Default
    private lateinit var weatherServiceClient: WeatherService

    @Inject
    @field:Default
    private lateinit var notificationServiceClient: NotificationService

    @Inject
    @field:Default
    private lateinit var userRepository: UserRepository

    var seqNum = 0

    @Transactional
    fun createUser(id: String, active: Boolean?, lat: Float?, lon: Float?, timeIntervalH: Int?, token: String?): Boolean? {
        val finalLat = lat ?: 0f
        val finalLon = lon ?: 0f
        val latLon = LatLon(finalLat,finalLon)
        val finalTimeIntervalH = timeIntervalH ?: 5
        val finalActive = active ?:false

        if(userRepository.get(id) != null){
            return null
        }

        userRepository.createUser(
            User(
                id = id,
                seqNum = seqNum,
                finalActive,
                latLon,
                finalTimeIntervalH,
                token ?: "",
            )
        )
        seqNum++

        if (token != null && !notificationServiceClient.createToken(id, token)){
            throw RuntimeException("failed to create Token")
        }

        if(!weatherServiceClient.createSubscription(
                id,
                timeIntervalH = finalTimeIntervalH,
                lon = latLon.lon,
                lat = latLon.lat,
                active = finalActive
            )
        ){
            notificationServiceClient.deleteToken(id)
            throw RuntimeException("failed to create Subscription")
        }
        return true
    }
    @Transactional
    fun updateUser(id: String, user: User): Boolean? {
        if(userRepository.get(id) == null)
            return null

        // update Token
        if(user.token != userRepository.get(id)?.token){
            if(userRepository.get(id)?.token?.isEmpty() == true) { // Token was not yet initialized
                if(!notificationServiceClient.createToken(user.id, user.token)){
                    throw RuntimeException("Failed to create Token")
                }
            }else {
                if(!notificationServiceClient.updateToken(user.id, user.token)){
                    throw RuntimeException("Failed to update Token")
                }
            }
        }
        userRepository.update(user)

        if(!weatherServiceClient.updateSubscription(user.id, user.timeIntervalH, user.latLon.lon, user.latLon.lat, user.active)){
            throw RuntimeException("Failed to update Subscription")
        }
        return true
    }
    @Transactional
    fun removeUser(id: String): Boolean?{
        if (userRepository.get(id) == null) return null

        userRepository.delete(id)

        if(!weatherServiceClient.deleteSubscription(id)){
            // TODO remove comment
            //throw RuntimeException("Failed to delete subscription")
        }
        if(!notificationServiceClient.deleteToken(id)){
            // TODO remove comment
            //throw RuntimeException("Failed to delete Token")
        }
        return true
    }

    fun getUser(id: String): User? = userRepository.get(id)

    fun getUsers(): List<User> = userRepository.listAll()



}