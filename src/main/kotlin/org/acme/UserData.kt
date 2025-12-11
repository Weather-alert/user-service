package org.acme

import java.sql.Time

data class User(val id: Int,
                val name: String,
                var active: Boolean = false,
                var latLng: LatLon = LatLon(0f, 0f),
                var notifyInterval: Long = 1*60*100 // default is a minute
)

data class LatLon(var lat: Float, var lon: Float)
