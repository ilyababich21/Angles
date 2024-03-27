package com.example.angles.service

import android.bluetooth.BluetoothSocket
import android.util.Log
import com.example.angles.model.Orientation
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class ReceiverThread :Thread() {

    private lateinit var mSocket:BluetoothSocket
    private lateinit var mmOutStream: OutputStream
    private lateinit var mmInputStream: InputStream
    private var btBuffer: ByteArray = ByteArray(1024)
    private var orientation:Orientation = Orientation(pitch = 0.0, roll = 0.0, yaw = 0.0)
    override fun run() {
        if (mSocket == null) return
        mmInputStream = mSocket.inputStream
        mmOutStream = mSocket.outputStream
        while (true){
            var length = mmInputStream.read(btBuffer)
            var inputMSG = String(btBuffer, 0, length ?: 0)
            Log.d("MyLog", length.toString() + String(btBuffer, 0, length ?: 0))
            if (inputMSG == "hello"){
                var msg = "roll: ${orientation.roll},pitch: ${orientation.pitch},yaw: ${orientation.yaw}\n"

                sendData(msg)
            }
            sleep(1000)
        }
    }
    fun sendData(message: String) {
        val runnable = Runnable {
            try {
                mmOutStream.write(message.toByteArray())
            } catch (e: IOException) {

            }
        }
        val thread = Thread(runnable)
        thread.start()
    }
    fun setSocket(mmSocket: BluetoothSocket) {
        mSocket = mmSocket
    }

    fun setOrientation(orientation: Orientation){
        this.orientation = orientation
    }
}