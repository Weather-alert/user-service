package org.acme

import java.sql.Time

data class User(val id: Int,
                val name: String,
                var active: Boolean = false,
                var latLon: LatLon = LatLon(0f, 0f),
                var notifyInterval: Int = 1 // default is an hour
)

data class LatLon(var lat: Float, var lon: Float)
