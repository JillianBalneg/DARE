package com.example.dare

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import com.example.dare.databinding.ActivityAbsBeginnerBinding

class AbsBeginnerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAbsBeginnerBinding
    private lateinit var absbArrayList: ArrayList<absBeginnerString>

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityAbsBeginnerBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val imageId = intArrayOf(

            R.drawable.ic_baseline_home_24,
            R.drawable.ic_baseline_person_24,
            R.drawable.ic_baseline_home_24,
            R.drawable.ic_baseline_home_24,
            R.drawable.ic_baseline_home_24,
            R.drawable.ic_baseline_home_24
        )

        val name = arrayOf(
            "Jumping Jacks",
            "Heel Touch",
            "Abdominal Crunches",
            "Russian Twist",
            "Leg Raises",
            "Plank"
        )

        val desc = arrayOf(
            "Jumping Jacks description",
            "Heel Touch description",
            "Abdominal Crunches description",
            "Russian Twist description",
            "Leg Raises description",
            "Plank description"
        )

        val timeAndCount = arrayOf(
            "20:00",
            "x16",
            "x20",
            "x20",
            "x16",
            "20:00",
        )

        absbArrayList = ArrayList()

        for (i in name.indices) {

            val absB = absBeginnerString(name[i], desc[i], imageId[i], timeAndCount[i])

            absbArrayList.add(absB)
        }

        binding.bAbsListview.isClickable = true
        binding.bAbsListview.adapter = MyAdaptor(this, absbArrayList)
        binding.bAbsListview.setOnItemClickListener { parent, view, position, id ->


            val name = name[position]
            val desc = desc[position]
            val imageId = imageId[position]
            val timeAndCount = timeAndCount[position]

            val i = Intent(this, PopUpWindow::class.java)

            i.putExtra("name", name)
            i.putExtra("desc", desc)
            i.putExtra("imageId", imageId)
            i.putExtra("timeAndCount", timeAndCount)
            startActivity(i)

        }

        binding.btnStart.setOnClickListener {
            startActivity(Intent(this, JumpingJacksActivity::class.java))
        }
    }
}