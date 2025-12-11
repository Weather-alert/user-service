package org.acme

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import jakarta.inject.Inject
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.Matchers.greaterThan
import org.hamcrest.core.IsEqual.equalTo
import org.junit.jupiter.api.Test

@QuarkusTest
class UserResourceTest {


    @Test
    fun testCreateAndGetUser() {
        // Create user with name "John"
        val name ="John"
        val active = false
        val latLng = LatLon(10f,11f)
        val notifyInterval = 1*60

        given()
            .post("/users?name=$name&active=$active&lat=${latLng.lat}&lon=${latLng.lon}&notifyInterval=$notifyInterval")
            .then()
            .statusCode(204) // Because createUser returns Unit

        // Verify user exists with name = John
        given()
            .get("/users/0")
            .then()
            .statusCode(200)
            .body("name", equalTo(name))
            .body("active", equalTo(active))
            .body("latLng.lat", equalTo(latLng.lat))
            .body("latLng.lon",equalTo(latLng.lon))
            .body("notifyInterval", equalTo(notifyInterval))
        given()
            .delete("/users/0")
            .then()
            .statusCode(204)
    }

    @Test
    fun testReturnUsers() {
        // Create a user "Jane"
        given()
            .post("/users?name=Jane")
            .then()
            .statusCode(204)

        // Validate response contains at least one user
        given()
            .get("/users")
            .then()
            .statusCode(200)
            .body("size()", greaterThan(0))
        given()
            .delete("/users/0")
            .then()
            .statusCode(204)
    }
}