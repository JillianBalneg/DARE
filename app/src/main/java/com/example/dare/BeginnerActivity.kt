package com.example.dare

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dare.databinding.ActivityBeginnerBinding
import com.example.dare.databinding.ActivitySelectorBinding


class BeginnerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBeginnerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBeginnerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.absB.setOnClickListener {
            startActivity(Intent(this, AbsBeginnerActivity::class.java))

        }
        binding.armsB.setOnClickListener {
            startActivity(Intent(this, AbsBeginnerActivity::class.java))

        }
        binding.legsB.setOnClickListener {
            startActivity(Intent(this, AbsBeginnerActivity::class.java))

        }
        binding.chestB.setOnClickListener {
            startActivity(Intent(this, AbsBeginnerActivity::class.java))

        }
    }
}