package com.example.angles.service

import android.app.Application
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import androidx.core.content.getSystemService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.angles.model.Orientation

class ServiceRepository(application: Application): SensorEventListener {

    private var isCon:MutableLiveData<String> = MutableLiveData("not Connect")
    private var orientation:MutableLiveData<Orientation> = MutableLiveData(Orientation(pitch = 0.0, roll = 0.0, yaw = 0.0))
    private val accelerometerReading = FloatArray(3)
    private val magnetometerReading = FloatArray(3)

    private val rotationMatrix = FloatArray(9)
    private val orientationAngles = FloatArray(3)

    val sensorManager:SensorManager = application.applicationContext.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    init{
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also { accelerometer ->
            sensorManager.registerListener(
                this,
                accelerometer,
                SensorManager.SENSOR_DELAY_NORMAL,
                SensorManager.SENSOR_DELAY_UI
            )
        }
        sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)?.also { magneticField ->
            sensorManager.registerListener(
                this,
                magneticField,
                SensorManager.SENSOR_DELAY_NORMAL,
                SensorManager.SENSOR_DELAY_UI
            )
        }

    }


    fun getCon():LiveData<String>{
        return isCon
    }

    fun getOrient():LiveData<Orientation>{
        return orientation
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                System.arraycopy(event.values, 0, accelerometerReading, 0, accelerometerReading.size)
            } else if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
                System.arraycopy(event.values, 0, magnetometerReading, 0, magnetometerReading.size)
            }
        updateOrientationAngles()
        }


    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }


    fun updateOrientationAngles() {
        // Update rotation matrix, which is needed to update orientation angles.
        SensorManager.getRotationMatrix(
            rotationMatrix,
            null,
            accelerometerReading,
            magnetometerReading
        )

        // "rotationMatrix" now has up-to-date information.

        SensorManager.getOrientation(rotationMatrix, orientationAngles)
        Log.d("MyLog",(orientationAngles[0]*57.2958).toString()+"     " + (orientationAngles[1]*57.2958).toString()+"     "+(orientationAngles[2]*57.2958).toString())
        orientation.postValue(Orientation(yaw=orientationAngles[0]*57.2958,roll = orientationAngles[1]*57.2958, pitch = orientationAngles[2]*57.2958))
        // "orientationAngles" now has up-to-date information.
    }

}