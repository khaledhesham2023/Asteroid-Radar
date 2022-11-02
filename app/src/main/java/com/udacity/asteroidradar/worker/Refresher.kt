package com.udacity.asteroidradar.worker

import android.app.Application
import android.content.Context
import android.os.Build
import androidx.work.*
import com.udacity.asteroidradar.Room.getAsteroidDatabase
import com.udacity.asteroidradar.repository.AsteroidsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.util.concurrent.TimeUnit

class Refresher(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "RefresherWork"
    }

    override suspend fun doWork(): Result {
        val database = getAsteroidDatabase(applicationContext)
        val repository = AsteroidsRepository(database)
        return try {
            repository.refreshListOfAsteroids()
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }
    }
}

// a class to cache the data when device is charging and wifi enabled
class AppWorker : Application() {
    // instance of application coroutineScope
    val applicationCoroutine = CoroutineScope(Dispatchers.Default)

    // overriding onCreate() method to use delayedInit functions
    override fun onCreate() {
        super.onCreate()
        delayedInit()
    }

    private fun delayedInit() = applicationCoroutine.launch {
        setupRecuuringWork()
    }

    private fun setupRecuuringWork() {
        // setting up constraints that application will work and use functions according to it such as
        // NetworkType, if battery requires charging, No low battery, setting idle device
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .setRequiresBatteryNotLow(true)
            .setRequiresCharging(true)
            .apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    setRequiresDeviceIdle(true)
                }
            }.build()
        // to refresh a list once a day
        val repeatingRequest =
            PeriodicWorkRequestBuilder<Refresher>(1, TimeUnit.DAYS).setConstraints(constraints)
                .build()

        // WorkerManager instance
        WorkManager.getInstance().enqueueUniquePeriodicWork(
            Refresher.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRequest
        )
    }
}