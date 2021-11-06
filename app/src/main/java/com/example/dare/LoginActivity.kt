package com.example.dare

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.example.dare.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    //viewBinding
    private lateinit var binding: ActivityLoginBinding

    //Actionbar
    private lateinit var actionBar: ActionBar

    //ProgressDialog
    private lateinit var progressDialog: ProgressDialog

    //FireBaseAuth
    private lateinit var firebaseAuth: FirebaseAuth
    private var email = ""
    private var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //config actionBar
        //val actionBar = supportActionBar!!
        //actionBar.title = "Login"

        //progress Dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("please wait")
        progressDialog.setMessage("Logging in...")
        progressDialog.setCanceledOnTouchOutside(false)

        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        //handle click open signupAct
        binding.noAccountTv.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
        //handle click, begin login
        binding.loginBtn.setOnClickListener {
            //before logged in, validate data
            validateData()
        }
        //later btn
        binding.laterBtn.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

    }
    private fun validateData() {
        //get data

        email = binding.emailEt.text.toString().trim()
        password = binding.passwordEt.text.toString().trim()

        //validate data
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            //invalid email format
            binding.emailEt.error = "invalid email format"
        }
        else if (TextUtils.isEmpty(password)) {
            //no password entered
            binding.passwordEt.error = "please enter password"
        }
        else{
            //data is validate, begin login
            firebaseLogin()
        }
    }
    private fun firebaseLogin() {
        // show progress
        progressDialog.show()
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                //login success
                progressDialog.dismiss()
                //get user info
                val firebaseUser = firebaseAuth.currentUser
                val email = firebaseUser!!.email
                Toast.makeText(this, "LoggedIn as $email", Toast.LENGTH_SHORT).show()
                //open profile
                startActivity(Intent(this, ProfileActivity::class.java))
                finish()
            }.addOnFailureListener { e ->
                //login failed
                progressDialog.dismiss()
                Toast.makeText(this, "login failed due to ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun checkUser() {
        //if user already logged in go to profile act
        //get current user
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null) {
            //user is logged in
            startActivity(Intent(this, ProfileActivity::class.java))
            finish()
        }
    }
}