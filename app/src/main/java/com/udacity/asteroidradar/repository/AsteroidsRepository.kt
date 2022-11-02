package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.Room.AsteroidDatabase
import com.udacity.asteroidradar.Room.asDomainModel
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.api.asDatabaseModel
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class AsteroidsRepository(private val asteroidDatabase: AsteroidDatabase) {

    // current day formatting
    val calendar = Calendar.getInstance()
    // setting up date formatter to convert date to string
    val dateFormat = SimpleDateFormat(
        Constants.API_QUERY_DATE_FORMAT,
        Locale.getDefault()
    )
    // instances of today and week
    val today = formatToday(calendar,dateFormat)
    val week = getWeek(calendar,dateFormat)

    // instance of all asteroids liveData
    val asteroidsInDatabase: LiveData<List<Asteroid>> =
        Transformations.map(asteroidDatabase.asteroidDao.getAsteroids()) {
            it.asDomainModel()
        }

    // instance of today asteroids liveData
    val asteroidsInDatabaseToday: LiveData<List<Asteroid>> =
        Transformations.map(asteroidDatabase.asteroidDao.getTodayAsteroids(today)) {
            it.asDomainModel()
        }

    // instance of weekly asteroids liveData
    val asteroidsInDatabaseWeek: LiveData<List<Asteroid>> =
        Transformations.map(asteroidDatabase.asteroidDao.getWeekAsteroids(today,week)) {
            it.asDomainModel()
        }

// refresh lists of the asteroids according to inserted start and end dates
    suspend fun refreshListOfAsteroids() {
        withContext(Dispatchers.IO) {
            val asteroids =
                AsteroidApi.retrofitService.getAsteroidApiResponse(Constants.API_KEY)
            val asteroidParsed = parseAsteroidsJsonResult(JSONObject(asteroids))
            asteroidDatabase.asteroidDao.insertAllAsteroids(*asteroidParsed.asDatabaseModel())
        }
    }

    /**
     * A function to format current day using calender and simple date format
     * @param calendar to set time
     * @param dateFormat to format date into a string
     * @return current day date in String format
     */
    private fun formatToday(calendar: Calendar,dateFormat: SimpleDateFormat): String {
        val currentTime = calendar.time
        return dateFormat.format(currentTime)
    }

    /**
     * A function to format 7th day using calender and simple date format
     * @param calendar to set time
     * @param dateFormat to format date into a string
     * @return 7th day date in String format
     */
    private fun getWeek(calendar: Calendar,dateFormat: SimpleDateFormat):String {
        calendar.add(Calendar.DAY_OF_YEAR, 7)
        val lastTime = calendar.time
        return dateFormat.format(lastTime)
    }
}





