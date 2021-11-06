package com.example.dare

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import com.example.dare.databinding.ActivityMountainClimberMainBinding
import java.util.concurrent.TimeUnit

class MountainClimberMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMountainClimberMainBinding

    companion object {
        const val TAG = "MountainClimberMainActivity"
    }
    private var isStarted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMountainClimberMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startTimer()

    }

    private var countDownTimer = object : CountDownTimer(1000 * 20, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            Log.d(TAG, "onTick: ${millisUntilFinished / 1000f}")
            binding.timeCountTv.text = getString(
                R.string.formatted_time,
                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60
            )
        }

        override fun onFinish() {
            Log.d(TAG, "onFinish: called")
            isStarted = false
            binding.startStopBtn.text = "Start"
            rest()
        }
    }
    private fun rest(){
        startActivity(Intent(this, RestActivity4::class.java))
    }

    private fun startTimer() {
        countDownTimer.start()
        isStarted = true
        binding.startStopBtn.text = "Stop"
    }

    private fun stopTimer() {
        countDownTimer.cancel()
        isStarted = false
        binding.startStopBtn.text = "Start"
        binding.timeCountTv.text = "00:30"

    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer.cancel()
    }

}