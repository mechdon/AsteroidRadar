package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.Network
import com.udacity.asteroidradar.api.getSeventhDay
import com.udacity.asteroidradar.api.getToday
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.repository.AsteroidsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AsteroidDatabase.getDatabase(application)
    private val asteroidsRepository = AsteroidsRepository(database)

    private var _asteroids = MutableLiveData<List<Asteroid>>()
    val asteroids: LiveData<List<Asteroid>>
        get() = _asteroids

    private val _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay>
        get() = _pictureOfDay

    private val _navigateToDetailFragment = MutableLiveData<Asteroid>()
    val navigateToDetailFragment: LiveData<Asteroid>
        get() = _navigateToDetailFragment

    init {
        onMenuSelectWeekAsteroids()
        viewModelScope.launch {
            refreshPictureOfDay()
            asteroidsRepository.refreshAsteroids()
        }
    }

    private suspend fun refreshPictureOfDay() {
        _pictureOfDay.value = getPictureOfDay()
    }

    fun onAsteroidClicked(asteroid: Asteroid){
        _navigateToDetailFragment.value = asteroid
    }

    fun doneNavigating(){
        _navigateToDetailFragment.value = null
    }

    suspend fun getPictureOfDay(): PictureOfDay? {
        var pictureOfDay = PictureOfDay("", "", "")
        withContext(Dispatchers.IO){
            pictureOfDay = Network.retrofitService.getPictureOfDay(BuildConfig.API_KEY).await()
        }
        if(pictureOfDay.mediaType == "image"){
            return pictureOfDay
        }
        return null
    }

    fun onMenuSelectWeekAsteroids(){
        viewModelScope.launch {
            database.asteroidDao.getAsteroids(getToday(), getSeventhDay())
                    .collect { asteroids ->
                        _asteroids.value = asteroids
                    }
        }
    }

    fun onMenuSelectTodayAsteroids(){
        viewModelScope.launch {
            database.asteroidDao.getAsteroids(getToday(), getToday())
                    .collect { asteroids ->
                        _asteroids.value = asteroids
                    }
        }
    }


    fun onMenuSelectSavedAsteroids(){
        viewModelScope.launch {
           database.asteroidDao.getAllAsteroids()
                   .collect { asteroids ->
                       _asteroids.value = asteroids
                   }
        }
    }


}