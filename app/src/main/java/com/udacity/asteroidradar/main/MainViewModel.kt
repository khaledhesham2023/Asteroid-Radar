package com.udacity.asteroidradar.main

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.Room.getAsteroidDatabase
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.repository.AsteroidsRepository
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    // instance of database
    private val database = getAsteroidDatabase(application)

    // instance of repository
    private val asteroidsRepository = AsteroidsRepository(database)

    // a mediator liveData that adds or removes repositories according to options selected
    var asteroids: MediatorLiveData<List<Asteroid>> = MediatorLiveData<List<Asteroid>>()


    // image of the day
    private var _image = MutableLiveData<String?>()
    val image: MutableLiveData<String?>
        get() = _image

    // title of the image
    private var _title = MutableLiveData<String>()
    val title: LiveData<String>
        get() = _title

    // progress wheel liveData that controls visibility of progress wheel
    private var _viewProgress = MutableLiveData<Boolean>()
    val viewProgress: LiveData<Boolean>
        get() = _viewProgress


    init {
        viewModelScope.launch {
            try {
                // adding all asteroids to list in main screen
                asteroids.addSource(asteroidsRepository.asteroidsInDatabase) {
                    asteroids.value = it
                }
                _viewProgress.value = true
                // update the asteroid list in database
                asteroidsRepository.refreshListOfAsteroids()
                // getting image of the day by calling an API based on API_KEY
                val pictureOfDay = AsteroidApi.retrofitService.getImageOfDayData(Constants.API_KEY)
                if (pictureOfDay.mediaType.equals("image")) {
                    _image.value = pictureOfDay.url
                    _title.value = pictureOfDay.title
                    // using sharedPreferences to store the image and imageTitle to get them offline
                    val sharedPreferences =
                        application.getSharedPreferences("application", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("imageOfDay", _image.value)
                    editor.putString("imageTitle", _title.value)
                    editor.apply()
                    _viewProgress.value = false
                } else {
                    // when media type is a video
                    _image.value = null
                    _title.value = pictureOfDay.title.toString()
                    _viewProgress.value = false
                }

            } catch (e: Exception) {
                // actions done in offline mode such as getting latest image and its title
                _image.value = application.getSharedPreferences("application", Context.MODE_PRIVATE)
                    .getString("imageOfDay", "")
                _title.value = application.getSharedPreferences("application", Context.MODE_PRIVATE)
                    .getString("imageTitle", "")
                _viewProgress.value = false

            }
        }
    }

    // function used to get list of all asteroids when select saved asteroids option is selected
    fun getAllAsteroids() {
        clearAllSources()
        asteroids.addSource(asteroidsRepository.asteroidsInDatabase) {
            asteroids.value = it

        }
    }

    // function used to get list of today asteroids when select today asteroids option is selected
    fun getTodayAsteroids() {
        clearAllSources()
        asteroids.addSource(asteroidsRepository.asteroidsInDatabaseToday) {
            asteroids.value = it
        }
    }

    // function used to get list of weekly asteroids when select week asteroids option is selected
    fun getWeekAsteroids() {
        clearAllSources()
        asteroids.addSource(asteroidsRepository.asteroidsInDatabaseWeek) {
            asteroids.value = it
        }

    }

    // this function is used to clear all asteroids mediatorLiveData sources before adding new one
    // to avoid multiple sources exception
    private fun clearAllSources() {
        asteroids.removeSource(asteroidsRepository.asteroidsInDatabase)
        asteroids.removeSource(asteroidsRepository.asteroidsInDatabaseToday)
        asteroids.removeSource(asteroidsRepository.asteroidsInDatabaseWeek)
    }
}