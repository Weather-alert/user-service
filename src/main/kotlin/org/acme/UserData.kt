package org.acme

import java.sql.Time

data class User(val seqNum: Int,
                val id: String,
                var active: Boolean = false,
                var latLon: LatLon = LatLon(0f, 0f),
                var timeIntervalH: Int = 1 // default is an hour
)

data class LatLon(var lat: Float, var lon: Float)
