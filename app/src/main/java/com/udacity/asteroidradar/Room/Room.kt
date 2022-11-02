package com.udacity.asteroidradar.Room

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface AsteroidDao {

    // a query method to get all selected asteroids
    @Query("SELECT * FROM asteroidsentity")
    fun getAsteroids(): LiveData<List<AsteroidsEntity>>

    // a query method to get Today's asteroids
    @Query("SELECT * FROM asteroidsentity WHERE closeApproachDate == :closeApproachDate")
    fun getTodayAsteroids(closeApproachDate: String): LiveData<List<AsteroidsEntity>>

    // a query method to get Week's asteroids
    @Query("SELECT * FROM asteroidsentity WHERE closeApproachDate BETWEEN :closeApproachDateToday AND :closeApproachDateWeek")
    fun getWeekAsteroids(
        closeApproachDateToday: String,
        closeApproachDateWeek: String
    ): LiveData<List<AsteroidsEntity>>


    // an upsert method to update the list of asteroids
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllAsteroids(vararg asteroid: AsteroidsEntity)
}

@Database(entities = [AsteroidsEntity::class], version = 1)
abstract class AsteroidDatabase : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao
}

// SINGLETON
private lateinit var INSTANCE: AsteroidDatabase

// getting a database
fun getAsteroidDatabase(context: Context): AsteroidDatabase {
    synchronized(AsteroidDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE =
                Room.databaseBuilder(
                    context.applicationContext,
                    AsteroidDatabase::class.java,
                    "asteroids"
                ).build()
        }
    }
    return INSTANCE
}
