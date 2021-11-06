package com.example.dare

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.dare.callback.stepsCallback
import com.example.dare.databinding.ActivityStepCounterBinding
import com.example.dare.helper.GeneralHelper
import com.example.dare.service.StepDetectorService

class StepCounterActivity : AppCompatActivity(), stepsCallback {

    private lateinit var binding: ActivityStepCounterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStepCounterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = Intent(this, StepDetectorService::class.java)
        startService(intent)

        StepDetectorService.subscribe.register(this)

    }

    override fun subscribeSteps(steps: Int) {
        binding.tvSTEPS.setText(steps.toString())
        binding.tvCALORIES.setText(GeneralHelper.getCalories(steps))
        binding.tvDISTANCE.setText(GeneralHelper.getDistanceCovered(steps))
    }
}