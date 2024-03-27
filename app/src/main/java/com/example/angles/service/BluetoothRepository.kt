package com.example.angles.service

import android.app.Application
import android.bluetooth.BluetoothManager
import android.content.Context
import androidx.core.content.getSystemService
import androidx.lifecycle.LiveData
import com.example.angles.model.Orientation

class BluetoothRepository(application: Application) {

    val bluetoothManager: BluetoothManager =
        application.applicationContext.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    val bleAdapter = bluetoothManager.adapter
    private var acceptThread: AcceptThread? = null

    init {
            acceptThread = AcceptThread(bleAdapter)
            acceptThread?.start()
    }

    fun changeOrient(orientation: Orientation){
        acceptThread?.receiverThread?.setOrientation(orientation)
    }

}