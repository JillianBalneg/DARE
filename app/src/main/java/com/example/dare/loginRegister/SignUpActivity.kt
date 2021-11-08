package com.example.dare.loginRegister

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import com.example.dare.bmi.NewProfileActivity
import com.example.dare.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference

class SignUpActivity : AppCompatActivity() {

    //viewBinding
    private lateinit var binding: ActivitySignUpBinding
    //progressDialog
    private lateinit var progressDialog: ProgressDialog
    //firebase
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference


    private var email = ""
    private var password = ""
    private var name = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)


        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("please wait")
        progressDialog.setMessage("Creating account in...")
        progressDialog.setCanceledOnTouchOutside(false)

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance()

        binding.signUpBtn.setOnClickListener {
            validateData()
        }
    }
    private fun validateData() {
        //get data
        email = binding.emailEt.text.toString().trim()
        password = binding.passwordEt.text.toString().trim()

        //validate data
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            //invalid email format
            binding.emailEt.error = "invalid email format"
        }
        else if (TextUtils.isEmpty(password)){
            //password isn't entered
            binding.passwordEt.error = "please enter password"
        }
        else if (password.length < 6){
            //password length is less than 6
            binding.passwordEt.error = "password must at least 6 characters long"
        }
        else{
            //data is valid, continue signup
            firebaseSignUp()
        }

    }
    private fun firebaseSignUp() {

        // show progress
        progressDialog.show()
        //create account
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                //Signup success
                progressDialog.dismiss()
                val firebaseUser = firebaseAuth.currentUser
                val email = firebaseUser!!.email
                Toast.makeText(this, "Account Created with Email  $email", Toast.LENGTH_LONG).show()

                //open profile
                val i = Intent(this,NewProfileActivity::class.java)
                i.putExtra("name",binding.nameEt.text.toString())
                startActivity(i)

                finish()
            }
            .addOnFailureListener{e->
                //signup Failed
                progressDialog.dismiss()
                Toast.makeText(this, "SignUp Failed due to ${e.message}", Toast.LENGTH_LONG).show()
            }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()// go back to previous activity when back button of actionbar clicked
        return super.onSupportNavigateUp()
    }
}