package com.example.angles.service

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.util.Log
import java.io.IOException
import java.util.UUID

class AcceptThread(bluetoothAdapter: BluetoothAdapter?) : Thread() {
    private val uuid = "00001101-0000-1000-8000-00805F9B34FB"
    private val name = "AndroidBLUE"
    var receiverThread :ReceiverThread = ReceiverThread()
    private val mmServerSocket: BluetoothServerSocket? by lazy(LazyThreadSafetyMode.NONE) {
        try {
            bluetoothAdapter?.listenUsingInsecureRfcommWithServiceRecord(
                name,
                UUID.fromString(uuid)
            )
        } catch (e: SecurityException) {
            return@lazy null
        }
    }

    override fun run() {
        // Keep listening until exception occurs or a socket is returned.
        var shouldLoop = true
        while (shouldLoop) {
            Log.d("MyLog", "Run server")

            val socket: BluetoothSocket? = try {
                mmServerSocket?.accept()
            } catch (e: IOException) {
                Log.d("MyLog", "Socket's accept() method failed$e")
                shouldLoop = false
                null
            }
            socket?.also {
                Log.d("MyLog", "Hello world")
                manageMyConnectedSocket(it)
                mmServerSocket?.close()
                shouldLoop = false
            }
        }
    }


    private fun manageMyConnectedSocket(socket: BluetoothSocket) {
        receiverThread.setSocket(socket)
        receiverThread.setCon(true)
        receiverThread.start()
    }


    fun cancel() {
        try {
            mmServerSocket?.close()

        } catch (e: IOException) {
            Log.d("MyLog", "Could not close the connect socket$e")
        }
    }
}