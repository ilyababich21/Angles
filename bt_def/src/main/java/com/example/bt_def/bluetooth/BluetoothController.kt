package com.example.bt_def.bluetooth

import android.bluetooth.BluetoothAdapter
import android.telephony.TelephonyCallback.EmergencyNumberListListener

class BluetoothController( private val adapter: BluetoothAdapter) {
    private var connectThread:ConnectThread? = null

    fun connect(mac:String,listener: Listener){
        if(adapter.isEnabled && mac.isNotEmpty()){

            val device = adapter.getRemoteDevice(mac)
            connectThread = ConnectThread(device, listener)
            connectThread?.start()
        }
    }

    fun sendMessage(message:String){
        connectThread?.recivierThread?.sendData(message)

    }

    fun closeConnection(){
        connectThread?.closeConnection()
    }

    companion object{
        const val BLUETOOTH_CONNECTED = "bluetooth_connected"
        const val BLUETOOTH_NO_CONNECTED = "bluetooth_no_connected"
    }

    interface Listener{
        fun onReceiver(message:String){

        }
    }
}