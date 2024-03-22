package com.example.bt_def.bluetooth

import android.bluetooth.BluetoothSocket
import android.os.Message
import android.util.Log
import androidx.lifecycle.MutableLiveData
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class ReceiverThread(  mSocket: BluetoothSocket, val listener: BluetoothController.Listener):Thread() {
       private val inputStream:InputStream = mSocket.inputStream
       private val outputStream:OutputStream = mSocket.outputStream
        private var btBuffer = ByteArray(1024)




    fun sendData(message: String){
        val runnable = Runnable {
            try{
                outputStream.write(message.toByteArray())
            }catch (e:IOException){
                listener.onReceiver(BluetoothController.BLUETOOTH_NO_CONNECTED)
            }
        }
        val thread = Thread(runnable)
        thread.start()
    }




    override fun run() {
        var lenght:Int
        while (true){
            try{
                lenght=inputStream.read(btBuffer)
                sleep(200)
                Log.d("MyLog", String(btBuffer,0,lenght?:0))

        }catch (e:IOException){
                listener.onReceiver(BluetoothController.BLUETOOTH_NO_CONNECTED)
        break
        }
        }
    }

}