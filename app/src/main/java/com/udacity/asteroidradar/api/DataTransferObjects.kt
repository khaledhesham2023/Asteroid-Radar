package com.udacity.asteroidradar.api

import com.squareup.moshi.JsonClass
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Room.AsteroidsEntity

// Transferring data by passing the retrieved asteroids list
@JsonClass(generateAdapter = true)
data class NetworkAsteroidsContainer(val asteroids: List<NetworkAsteroid>)

// Object used to store asteroid item in the list of asteroids
@JsonClass(generateAdapter = true)
data class NetworkAsteroid constructor(
    val id: Long,
    val codename: String,
    val closeApproachDate: String,
    val absoluteMagnitude: Double,
    val estimatedDiameter: Double,
    val relativeVelocity: Double,
    val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean
)

// Converting data to Data object(Domain module)
fun NetworkAsteroidsContainer.asDomainModel(): List<Asteroid> {
    return asteroids.map {
        Asteroid(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }
}


// Converting asteroids list into database module
fun ArrayList<Asteroid>.asDatabaseModel(): Array<AsteroidsEntity> {
    return map {
        AsteroidsEntity(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }.toTypedArray()
}