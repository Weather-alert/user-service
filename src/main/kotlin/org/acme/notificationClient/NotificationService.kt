package org.acme.notificationClient

import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.context.RequestScoped
import jakarta.ws.rs.core.Response
import org.eclipse.microprofile.rest.client.inject.RestClient

@ApplicationScoped
class NotificationService(
    @RestClient
    private val client: NotificationServiceClient
){
    fun createToken(userId: String, token: String): Boolean {
        return try {
            val r = client.createToken(userId, token)
            r.status == Response.Status.OK.statusCode
        } catch(e: Exception) {
            return false
        }
    }
    
    fun readToken(userId: String): String{
        return client.readToken(userId).entity.toString()
    }

    fun updateToken(userId: String, token: String): Boolean {
        return try {
            val r = client.updateToken(userId, token)
            r.status == Response.Status.OK.statusCode
        } catch(e: Exception) {
            return false
        }
    }
    fun deleteToken(userId: String): Boolean {
        return try {
            val r = client.deleteToken(userId)
            r.status == Response.Status.OK.statusCode
        } catch(e: Exception) {
            return false
        }
    }
}