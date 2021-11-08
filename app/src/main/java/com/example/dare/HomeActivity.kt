package com.example.dare

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.dare.bmi.NewProfileActivity

import com.example.dare.databinding.ActivityHomeBinding
import com.example.dare.loginRegister.LoginActivity
import com.example.dare.pedometer.StepCounterActivity

class HomeActivity : AppCompatActivity() {

    private lateinit var binding:ActivityHomeBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.d1.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        binding.d3.setOnClickListener {
            startActivity(Intent(this, SelectorActivity::class.java))
        }
        binding.d4.setOnClickListener {
            startActivity(Intent(this, StepCounterActivity::class.java))
        }


    }
}