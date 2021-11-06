package com.example.dare

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.dare.databinding.ActivityTestBinding

class TestActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra("name")
        val desc = intent.getStringExtra("desc")
        val imageId = intent.getIntExtra("imageId",R.drawable.workoutbeginner)

        binding.image.setImageResource(imageId)
        binding.nameAbs.text = name
        binding.descAbs.text = desc
    }
}