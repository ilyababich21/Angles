package com.example.angles.service

import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import androidx.lifecycle.LiveData

import com.example.angles.model.Orientation

class BluetoothRepository(application: Application) {

    private val bluetoothManager: BluetoothManager =
        application.applicationContext.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    private val bleAdapter: BluetoothAdapter = bluetoothManager.adapter
    private var acceptThread: AcceptThread? = null
    private lateinit var isConn: LiveData<Boolean>

    init {
        startDiscovery()
    }


    fun getCon(): LiveData<Boolean> {
        return isConn
    }

    fun startDiscovery() {
        acceptThread = AcceptThread(bleAdapter)
        isConn = acceptThread!!.receiverThread.getCon()
        acceptThread?.start()
    }

    fun changeOrient(orientation: Orientation) {
        acceptThread?.receiverThread?.setOrientation(orientation)
    }

}