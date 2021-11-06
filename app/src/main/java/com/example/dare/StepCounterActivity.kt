package com.example.dare

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.dare.databinding.ActivityStepCounterBinding

class StepCounterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStepCounterBinding

    private var mStepCounter: StepCounter? = null
    private var mSensorManager: SensorManager? = null
    private var mAccelerometer: Sensor? = null

    private var sensorOn = false
    private var steps = 0

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStepCounterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mAccelerometer = mSensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        mStepCounter = StepCounter(object : StepCounter.StepDetector {
            @SuppressLint("SetTextI18n")
            override fun onStepDetected() {
                steps += 1

                binding.tvSteps.text = steps.toString()
                binding.tvKilometers.text = Util.calculateKilometers(steps)
                binding.tvCalories.text = Util.calculateCalories(steps)
                binding.tvMainSteps.text = "Steps: $steps"
                //binding.ivOn.setImageDrawable(resources.getDrawable(R.drawable.on_element))
                startForegroundService(steps)
            }
        })

        binding.ivOn.setOnClickListener {
            sensorOn = !sensorOn
            if (sensorOn){
                startForegroundService(steps)
            } else{
                stopForegroundService()
            }
        }
    }

    private fun startForegroundService(steps: Int) {
        mSensorManager!!.registerListener(mStepCounter, mAccelerometer, SensorManager.SENSOR_DELAY_UI)
        //binding.ivOn.setImageDrawable(resources.getDrawable(R.drawable.on_element))

        val serviceIntent = Intent(this, PedometerService::class.java)
        serviceIntent.putExtra("steps", steps)
        startService(serviceIntent)
    }

    private fun stopForegroundService() {
        mSensorManager!!.unregisterListener(mStepCounter)
        //binding.ivOn.setImageDrawable(resources.getDrawable(R.drawable.off_element))

        val serviceIntent = Intent(this, PedometerService::class.java)
        stopService(serviceIntent)
    }

}