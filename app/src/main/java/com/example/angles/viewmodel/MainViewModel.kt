package com.example.angles.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.angles.model.Orientation
import com.example.angles.service.ServiceRepository

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var serviceRepository: ServiceRepository
    private lateinit var isCon: LiveData<String>
    private lateinit var orientation: LiveData<Orientation>


    init {
        try {
            serviceRepository = ServiceRepository(application)
            isCon = serviceRepository.getCon()
            orientation = serviceRepository.getOrient()
        } catch (e: Exception) {
            Log.d("MyLog", e.toString())
        }


    }

    fun getCon(): LiveData<String> {
        return isCon
    }

    fun getOrient():LiveData<Orientation>{
        return orientation
    }

}