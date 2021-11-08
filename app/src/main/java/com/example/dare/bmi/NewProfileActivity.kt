package com.example.dare.bmi

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import com.example.dare.R
import com.example.dare.databinding.ActivityNewProfileBinding
import com.example.dare.databinding.ResultMainBinding
import com.example.dare.bmi.model.Person
import com.example.dare.bmi.repository.MainRepository
import com.example.dare.state.DataState
import com.example.dare.bmi.util.PrefsUtil
import com.example.dare.bmi.util.state.StateEventCalc
import com.example.dare.bmi.util.Constants
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class NewProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewProfileBinding
    private lateinit var viewModel: MainViewModel
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

    }
    override fun onDestroy() {
        viewModelStore.clear()
        super.onDestroy()
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
            textHeight.setText(insertHeight)
            textWeight.setText(insertWeight)
            textAge.setText(insertAge)

            val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.apply{
                putString("STRING_KEY1", insertHeight)
                putString("STRING_KEY2", insertWeight)
                putString("STRING_KEY3", insertAge)
            }.apply()
        }
        Toast.makeText(this,"Data Saved",Toast.LENGTH_LONG).show()
    }

    private fun loadData(){
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val savedString1 = sharedPreferences.getString("STRING_KEY1", null)
        val savedString2 = sharedPreferences.getString("STRING_KEY2", null)
        val savedString3 = sharedPreferences.getString("STRING_KEY3", null)

        binding.run {
            textHeight.setText(savedString1)
            textWeight.setText(savedString2)
            textAge.setText(savedString3)
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
}