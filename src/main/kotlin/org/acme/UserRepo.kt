package org.acme

import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class UserRepo {

    private val userMap = mutableMapOf<Int, User>()

    var idSeq = 0

    fun addUser(name: String, active: Boolean, latlon: LatLon, notifyInterval: Long){
        userMap.put(idSeq, User(idSeq,name,active,latlon,notifyInterval))
        idSeq++
    }
    fun getUser(id: Int): User? = userMap[id]

    fun getUsers(): Map<Int, User> = userMap

    fun updateUser(id: Int, user: User) {
        userMap[id] = user
    }
    fun removeUser(id: Int) = userMap.remove(id)

}