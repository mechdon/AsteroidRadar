package com.udacity.asteroidradar.database

import android.content.Context
import androidx.room.*
import com.udacity.asteroidradar.Asteroid
import kotlinx.coroutines.flow.Flow

@Dao
interface AsteroidDao {

    @Query("select * from DatabaseAsteroid where closeApproachDate >= :startDate and closeApproachDate <= :endDate")
    fun getAsteroids(startDate: String, endDate: String): Flow<List<Asteroid>>

    @Query("select * from DatabaseAsteroid")
    fun getAllAsteroids(): Flow<List<Asteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroids: DatabaseAsteroid)

    @Query("delete from DatabaseAsteroid where closeApproachDate < :today")
    fun deletePreviousDayAsteroids(today: String): Int

}

@Database(entities = [DatabaseAsteroid::class], version = 1, exportSchema = false)
abstract class AsteroidDatabase: RoomDatabase() {

    abstract val asteroidDao: AsteroidDao

    companion object {

        private lateinit var INSTANCE: AsteroidDatabase

        fun getDatabase(context: Context): AsteroidDatabase {
            synchronized(AsteroidDatabase::class.java){
                if (!::INSTANCE.isInitialized){
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AsteroidDatabase::class.java,
                        "asteroids"
                    ).build()
                }
            }
            return INSTANCE
        }
    }
}