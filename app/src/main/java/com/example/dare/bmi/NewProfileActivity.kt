package com.example.dare.bmi

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import com.example.dare.HomeActivity
import com.example.dare.R
import com.example.dare.databinding.ActivityNewProfileBinding
import com.example.dare.databinding.ResultMainBinding
import com.example.dare.bmi.model.Person
import com.example.dare.bmi.repository.MainRepository
import com.example.dare.state.DataState
import com.example.dare.bmi.util.PrefsUtil
import com.example.dare.bmi.util.state.StateEventCalc
import com.example.dare.bmi.util.Constants
import com.example.dare.loginRegister.LoginActivity
import com.example.dare.loginRegister.User
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class NewProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewProfileBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private var count = 0F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadData()

        viewModel = ViewModelProvider(
            this,
            MainViewModel.MainViewModelFactory(MainRepository())
        ).get(MainViewModel::class.java)

        binding.cardMale.isChecked = true
        setTheme()
        setListeners()
        observeViewModelEvents()

        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        binding.logoutBtn.setOnClickListener {
            firebaseAuth.signOut()
            checkUser()
        }

    }
    override fun onDestroy() {
        viewModelStore.clear()
        super.onDestroy()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, HomeActivity::class.java))
    }

    fun onClickMain(view: View) {
        binding.run {
            when (view.id) {
                buttonHeightDec.id -> { // Height Button decrease
                    count = sliderHeight.value
                    count--
                    if (count <= 0) count = 0F
                    sliderHeight.value = count
                    textHeight.text = count.toInt().toString()
                }
                buttonHeightInc.id -> { // Height Button increase
                    count = sliderHeight.value
                    count++
                    val resValue = ResourcesCompat.getFloat(resources, R.dimen.height_value_to)
                    if (count >= resValue) count = resValue
                    sliderHeight.value = count
                    textHeight.text = count.toInt().toString()
                }
                buttonWeightDec.id -> { // Weight Button decrease
                    count = sliderWeight.value
                    count--
                    if (count <= 0) count = 0F
                    sliderWeight.value = count
                    textWeight.text = count.toInt().toString()
                }
                buttonWeightInc.id -> { // Weight Button increase
                    count = sliderWeight.value
                    count++
                    val resValue = ResourcesCompat.getFloat(resources, R.dimen.weight_value_to)
                    if (count >= resValue) count = resValue
                    sliderWeight.value = count
                    textWeight.text = count.toInt().toString()
                }
                buttonAgeDec.id -> { // Age Button decrease
                    count = sliderAge.value
                    count--
                    if (count <= 0) count = 0F
                    sliderAge.value = count
                    textAge.text = count.toInt().toString()
                }
                buttonAgeInc.id -> { // Age Button increase
                    count = sliderAge.value
                    count++
                    val resValue = ResourcesCompat.getFloat(resources, R.dimen.age_value_to)
                    if (count >= resValue) count = resValue
                    sliderAge.value = count
                    textAge.text = count.toInt().toString()
                }
                buttonCalculate.id -> {
                    // Calculate
                    val person = Person(getGender(), getHeight(), getWeight(), getAge())
                    viewModel.setStateEvent(person, StateEventCalc.StateEvent)
                    saveData()
                    proceed()
                }
            }
        }
    }

    private fun setListeners() {
        binding.run {
            // Cards Listeners
            cardMale.setOnClickListener {
                cardMale.isChecked = true
                cardFemale.isChecked = false
            }
            cardFemale.setOnClickListener {
                cardMale.isChecked = false
                cardFemale.isChecked = true
            }

            // Sliders Listeners
            sliderHeight.addOnChangeListener { _, value, _ ->
                textHeight.text = value.toInt().toString()
            }
            sliderWeight.addOnChangeListener { _, value, _ ->
                textWeight.text = value.toInt().toString()
            }
            sliderAge.addOnChangeListener { _, value, _ ->
                textAge.text = value.toInt().toString()
            }
        }
    }

    private fun saveData(){
        binding.run {
            val insertHeight: String = textHeight.text.toString()
            val insertWeight: String = textWeight.text.toString()
            val insertAge: String = textAge.text.toString()
            val insertName : String = nameTv.text.toString()

            textHeight.setText(insertHeight)
            textWeight.setText(insertWeight)
            textAge.setText(insertAge)
            nameTv.setText(insertName)

            val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.apply{
                putString("STRING_KEY1", insertHeight)
                putString("STRING_KEY2", insertWeight)
                putString("STRING_KEY3", insertAge)
                putString("STRING_KEY5", insertName)

            }.apply()
        }
        Toast.makeText(this,"Data Saved",Toast.LENGTH_LONG).show()
    }

    private fun loadData(){
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val savedString1 = sharedPreferences.getString("STRING_KEY1", null)
        val savedString2 = sharedPreferences.getString("STRING_KEY2", null)
        val savedString3 = sharedPreferences.getString("STRING_KEY3", null)
        val savedString4 = sharedPreferences.getString("STRING_KEY5", null)

        binding.run {
            textHeight.setText(savedString1)
            textWeight.setText(savedString2)
            textAge.setText(savedString3)
            nameTv.setText(savedString4)

            val name = intent.getStringExtra("name")
            nameTv.text = name

//            this part have bug
            val insertName : String = nameTv.text.toString()
            nameTv.setText(insertName)

            val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.apply{
                putString("STRING_KEY5", insertName)

            }.apply()
        }
    }

    // Observe Events from ViewModel
    private fun observeViewModelEvents() {
        viewModel.calcEvent.observe(this) { _dataState ->
            when (_dataState) {
                is DataState.Success -> {
                    val bmi = _dataState.data.bmi
                    val bfp = _dataState.data.bfp
                    val bmiTable = _dataState.data.bmiTable
                    dialogResult(bmi, bfp, bmiTable)
                }
                is DataState.Error -> {
                    Toast.makeText(
                        this,
                        _dataState.message, Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        viewModel.themeEvent.observe(this) {
            setTheme()
        }
    }

    // Get values from sliders and cards
    private fun getGender() = if (binding.cardMale.isChecked) Constants.MALE else Constants.FEMALE

    private fun getHeight() = binding.sliderHeight.value

    private fun getWeight() = binding.sliderWeight.value.toInt()

    private fun getAge() = binding.sliderAge.value.toInt()

    private fun dialogResult(bmi: String, bfp: String, @StringRes bmiTable: Int) {
        val binding = ResultMainBinding.inflate(layoutInflater)
        // BMI
        binding.halfGauge.text = bmi.toFloat().toString()
        // Convert from vararg to IntArray
        //binding.halfGauge.setLineColors(*Colors.array())

        // BFP
        binding.textBfpResult.text = bfp
        binding.textBmiTable.setText(bmiTable)

        // Show a dialog for result
        MaterialAlertDialogBuilder(this, R.style.AlertDialogAppearance)
            .setView(binding.root)
            .setPositiveButton(android.R.string.ok, null)
            .create()
            .show()
    }

    private fun setTheme() {
        // Get value from Shared Preferences Zero is default
        val child = PrefsUtil.getPref(this)
            .getInt(PrefsUtil.PREF_KEY_MODE_NIGHT, 0)
    }

    private fun checkUser(){
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

    private fun proceed(){
        val uEmail = binding.emailTv.text.toString().replace('.', '*')
        val uName = binding.nameTv.text.toString()
        val uAge = binding.textAge.text.toString()
        val uHeight = binding.textHeight.text.toString()
        val uWeight = binding.textWeight.text.toString()

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
}