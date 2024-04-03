package com.example.angles.service

import android.app.Application
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.angles.model.Orientation

class ServiceRepository(application: Application,private var listener:Listener): SensorEventListener {


    private var orientation:MutableLiveData<Orientation> = MutableLiveData(Orientation(pitch = 0.0, roll = 0.0, yaw = 0.0, alt = 0.0))
    private val accelerometerReading = FloatArray(3)
    private val magnetometerReading = FloatArray(3)
    private var pressureAtSeaLevel = 1013.25f

    private val rotationMatrix = FloatArray(9)
    private val orientationAngles = FloatArray(3)
    private  var pressure:Float = 0f

    private val sensorManager:SensorManager = application.applicationContext.getSystemService(Context.SENSOR_SERVICE) as SensorManager

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
        sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)?.also { pressureField->
            sensorManager.registerListener(
                this,
                pressureField,
                SensorManager.SENSOR_DELAY_NORMAL,
                SensorManager.SENSOR_DELAY_UI

            )
        }

    }




    fun getOrient():LiveData<Orientation>{
        return orientation
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            when (event.sensor.type) {
                Sensor.TYPE_ACCELEROMETER -> {
                    System.arraycopy(event.values, 0, accelerometerReading, 0, accelerometerReading.size)
                }
                Sensor.TYPE_MAGNETIC_FIELD -> {
                    System.arraycopy(event.values, 0, magnetometerReading, 0, magnetometerReading.size)
                    updateOrientationAngles()
                }
                Sensor.TYPE_PRESSURE -> {
                    pressure= event.values[0]
                }
            }
        }


    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }


    private fun updateOrientationAngles() {
        // Update rotation matrix, which is needed to update orientation angles.
        SensorManager.getRotationMatrix(
            rotationMatrix,
            null,
            accelerometerReading,
            magnetometerReading
        )

        // "rotationMatrix" now has up-to-date information.

        SensorManager.getOrientation(rotationMatrix, orientationAngles)
        val altitude = SensorManager.getAltitude(pressureAtSeaLevel,pressure)
//        Log.d("MyLogDog",(orientationAngles[0]*57.2958).toString()+"     " + (orientationAngles[1]*57.2958).toString()+"     "+(orientationAngles[2]*57.2958).toString())


        listener.changeOrient(Orientation(yaw=orientationAngles[0]*57.2958,roll = orientationAngles[1]*1.00, pitch = orientationAngles[2]*1.00, alt = altitude*1.00))
        orientation.postValue(Orientation(yaw=orientationAngles[0]*57.2958,roll = orientationAngles[1]*57.2958, pitch = orientationAngles[2]*57.2958, alt = altitude*1.00))
        // "orientationAngles" now has up-to-date information.
    }


    interface Listener{
        fun changeOrient(orientation: Orientation)
    }

}