package com.example.angles.service

import android.bluetooth.BluetoothSocket
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.angles.model.Orientation
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class ReceiverThread : Thread() {

    private lateinit var mSocket: BluetoothSocket
    private lateinit var mmOutStream: OutputStream
    private lateinit var mmInputStream: InputStream
    private var btBuffer: ByteArray = ByteArray(1024)
    private var orientation: Orientation = Orientation(pitch = 0.0, roll = 0.0, yaw = 0.0, alt = 0.0)
    private var isConn:MutableLiveData<Boolean> =  MutableLiveData(false)
    override fun run() {
        mmInputStream = mSocket.inputStream
        mmOutStream = mSocket.outputStream
        try {
            while (true) {
                Log.d("MyLog", "Listening...")
                val length = mmInputStream.read(btBuffer)
                val inputMSG = String(btBuffer, 0, length)
                Log.d("MyLog", length.toString() + String(btBuffer, 0, length))
                if (inputMSG == "hello") {
                    val msg =
                        "${orientation.pitch} ${orientation.roll} ${orientation.yaw} ${orientation.alt}"
//                        "roll: ${orientation.roll},pitch: ${orientation.pitch},yaw: ${orientation.yaw}\n"

                    sendData(msg)
                }
                else if (inputMSG == "exit"){
                    mSocket.close()
                    isConn.postValue(false)
                    break
                }
                else {
                    sendData("Neverno ukazana commanda...")
                }
                sleep(100)
            }
        } catch (e: IOException) {
            return
        }
    }

    private fun sendData(message: String) {
        val runnable = Runnable {
            try {
                mmOutStream.write(message.toByteArray())
            } catch (_: IOException) {

            }
        }
        val thread = Thread(runnable)
        thread.start()
    }

    fun getCon():LiveData<Boolean>{
        return isConn
    }

    fun setCon(value:Boolean){
        isConn.postValue(value)
    }

    fun setSocket(mmSocket: BluetoothSocket) {
        mSocket = mmSocket
    }

    fun setOrientation(orientation: Orientation) {
        this.orientation = orientation
    }
}