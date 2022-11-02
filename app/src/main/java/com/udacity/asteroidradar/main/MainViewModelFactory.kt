package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

// To use benefits of AndroidViewModel, use ViewModelFactory
class MainViewModelFactory(val application: Application): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                return MainViewModel(application) as T
            }
            throw IllegalArgumentException("No available viewModel")

        }
    }
