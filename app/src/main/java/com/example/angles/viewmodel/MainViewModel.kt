package com.example.angles.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.angles.model.Orientation
import com.example.angles.service.BluetoothRepository
import com.example.angles.service.ServiceRepository

class MainViewModel(application: Application) : AndroidViewModel(application),ServiceRepository.Listener {

    private lateinit var serviceRepository: ServiceRepository
    private lateinit var isCon: LiveData<Boolean>
    private lateinit var orientation: LiveData<Orientation>
    private lateinit var bluetoothRepository: BluetoothRepository


    init {
        try {
            bluetoothRepository = BluetoothRepository(application)
            serviceRepository = ServiceRepository(application,this@MainViewModel)
            isCon = bluetoothRepository.getCon()
            orientation = serviceRepository.getOrient()

        } catch (e: Exception) {
            Log.d("MyLog", e.toString())
        }


    }

    fun getCon(): LiveData<Boolean> {
        return isCon
    }

    fun getOrient():LiveData<Orientation>{
        return orientation
    }

    override fun changeOrient(orientation: Orientation) {
        bluetoothRepository.changeOrient(orientation)
    }

}