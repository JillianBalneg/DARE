package com.example.dare

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.dare.databinding.ActivitySelectorBinding


class SelectorActivity : AppCompatActivity() {

    private lateinit var binding:ActivitySelectorBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.selectorBeginner.setOnClickListener {
            startActivity(Intent(this, BeginnerActivity::class.java))

        }
        binding.selectorIntermediate.setOnClickListener {

        }
        binding.selectorAdvance.setOnClickListener {

        }
    }
}