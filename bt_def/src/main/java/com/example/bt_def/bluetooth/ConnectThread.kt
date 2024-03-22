package com.example.bt_def.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.util.Log
import java.io.IOException
import java.util.UUID

class ConnectThread(device: BluetoothDevice, val listener: BluetoothController.Listener) :
    Thread() {
    private val uuid = "00001101-0000-1000-8000-00805F9B34FB"
    private  var mSocket: BluetoothSocket? = null
     lateinit var recivierThread:ReceiverThread

    init {
        try {
            mSocket = device.createRfcommSocketToServiceRecord(UUID.fromString(uuid))
        } catch (e: IOException) {

        } catch (e: SecurityException) {

        }
    }

    override fun run() {

        try {
            mSocket?.connect()
            listener.onReceiver(BluetoothController.BLUETOOTH_CONNECTED)
            recivierThread = mSocket?.let { ReceiverThread(it,listener) }!!
            recivierThread.start()
        } catch (e: IOException) {
            listener.onReceiver(BluetoothController.BLUETOOTH_NO_CONNECTED)
            closeConnection()

        } catch (e: SecurityException) {

        }
    }

    private fun readMessage() {
        val buffer = ByteArray(1024)
        while (true) {
            try {

                var message = ""
                while (mSocket?.inputStream?.available()!! > 0) {


                    val length = mSocket?.inputStream?.read(buffer)

                    Log.d("MyLog", length.toString())

                    message += String(buffer, 0, length ?: 0)
                }

                if (message!="") listener.onReceiver(message)
            } catch (e: IOException) {
                break
            }
        }

    }

    fun sendMessage(message: String) {
        try {
            mSocket?.outputStream?.write(message.toByteArray())
        } catch (e: IOException) {
            listener.onReceiver(BluetoothController.BLUETOOTH_NO_CONNECTED)
        }


    }

    fun closeConnection() {
        try {
            mSocket?.close()
        } catch (e: IOException) {

        }
    }

}