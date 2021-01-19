package com.udacity.asteroidradar.repository

import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.Network
import com.udacity.asteroidradar.api.getSeventhDay
import com.udacity.asteroidradar.api.getToday
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidsRepository(private val database: AsteroidDatabase) {

    suspend fun refreshAsteroids(){
        var asteroidsList: ArrayList<Asteroid>
        withContext(Dispatchers.IO){
            val asteroidRes = Network.retrofitService.getAsteroids(getToday(), getSeventhDay(), Constants.API_KEY).await()
            asteroidsList = parseAsteroidsJsonResult(JSONObject(asteroidRes.string()))
            database.asteroidDao.insertAll(*asteroidsList.asDomainModel())
        }
    }

    suspend fun deletePreviousDayAsteroids() {
        withContext(Dispatchers.IO){
            database.asteroidDao.deletePreviousDayAsteroids(getToday())
        }
    }

}