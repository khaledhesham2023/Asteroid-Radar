package com.udacity.asteroidradar.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// An instance of Moshi converter to convert API data into an object
private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

// An instance of retrofit that uses both Scalars and Moshi factories to parse the Json response of asteroids list
private val retrofit =
    Retrofit.Builder()
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(Constants.BASE_URL)
        .build()

interface AsteroidApiService {
    // Calling the Image Of The Day data using API Key
    @GET("planetary/apod")
    suspend fun getImageOfDayData(@Query("api_key") key: String): PictureOfDay

    // Calling the list of asteroids existing from start date argument to end date argument
    @GET("neo/rest/v1/feed")
    suspend fun getAsteroidApiResponse(
        @Query("api_key") key: String
    ): String

}

// Using this object to connect API functions to application
object AsteroidApi {
    val retrofitService: AsteroidApiService by lazy {
        retrofit.create(AsteroidApiService::class.java)
    }
}