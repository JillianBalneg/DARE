package com.example.dare

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.example.dare.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.StorageReference

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var actionBar: ActionBar
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadData()

        //firebase
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        //handles logout
        binding.logoutBtn.setOnClickListener {
            firebaseAuth.signOut()
            checkUser()
        }
        //            REALTIME DATABASE Save
        binding.proceedBtn.setOnClickListener {
            proceed()
            saveData()
        }

        binding.getDataBtn.setOnClickListener {
            saveData()
        }
    }

    private fun proceed() {
        val uEmail = binding.emailTv.text.toString().replace('.', '*')
        val uName = binding.nameEt.text.toString()
        val uAge = binding.ageEt.text.toString()
        val uHeight = binding.heightEt.text.toString()
        val uWeight = binding.weightEt.text.toString()

        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        val user = User(uEmail, uName, uAge, uHeight, uWeight)
        databaseReference.child(uEmail).setValue(user).addOnSuccessListener {
            Toast.makeText(
                this,
                "Profile successfully updated",
                Toast.LENGTH_SHORT
            ).show()
        }.addOnFailureListener {
            Toast.makeText(
                this,
                "Failed to update profile",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun saveData(){
        val insertName: String = binding.nameEt.text.toString()
        binding.nameEt.setText(insertName)

        val insertAge: String = binding.ageEt.text.toString()
        binding.ageEt.setText(insertAge)

        val insertHeight: String = binding.heightEt.text.toString()
        binding.heightEt.setText(insertHeight)

        val insertWeight: String = binding.weightEt.text.toString()
        binding.weightEt.setText(insertWeight)

        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE )
        val editor = sharedPreferences.edit()
        editor.apply{
            putString("STRING_KEY1",insertName)
            putString("STRING_KEY2",insertAge)
            putString("STRING_KEY3",insertHeight)
            putString("STRING_KEY4",insertWeight)
        }.apply()
        Toast.makeText(this,"Data Saved",Toast.LENGTH_LONG).show()

    }

    private fun loadData(){
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val savedString1 = sharedPreferences.getString("STRING_KEY1",null)
        val savedString2 = sharedPreferences.getString("STRING_KEY2",null)
        val savedString3 = sharedPreferences.getString("STRING_KEY3",null)
        val savedString4 = sharedPreferences.getString("STRING_KEY4",null)

        binding.nameEt.setText(savedString1)
        binding.ageEt.setText(savedString2)
        binding.heightEt.setText(savedString3)
        binding.weightEt.setText(savedString4)
    }

    private fun checkUser() {
        //check user is login or not
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null) {
            //not null then logged in, get user info
            val email = firebaseUser.email
            //set to Tv
            binding.emailTv.text = email
        }
        else {
            //user null then not logged in
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}