package org.acme

import io.quarkus.hibernate.orm.panache.PanacheEntityBase
import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.sql.Time

data class UserUpdateRequest(
    var active: Boolean? = null,
    var latitude: Float? = null,
    var longitude: Float? = null,
    var timeIntervalH: Int? = null // default is an hour
)

@Entity
@Table(name = "users")
data class User(
    @Id
    var id: String = "",   // primary key // userId

    var seqNum: Int = 0,

    var active: Boolean = false,

    @Embedded
    var latLon: LatLon = LatLon(),

    var timeIntervalH: Int = 1,

    var token: String = ""
)

@Embeddable
data class LatLon(var lat: Float, var lon: Float){
    // Hibernate requires a no-arg constructor — the default values already serve this
    constructor() : this(0f, 0f)
}
