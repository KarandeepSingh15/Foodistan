package com.karandeepsingh.foodistan.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.karandeepsingh.foodistan.R

class LoginActivity : AppCompatActivity() {
    lateinit var etMobileNumber: EditText
    lateinit var etPassword: EditText
    lateinit var btnLogin: Button
    lateinit var txtForgotPassword: TextView
    lateinit var txtRegister: TextView
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        title = getString(R.string.login)
        var mobileNumber: String
        var password: String
        val validMobileNumber = "9818270539"
        val validPassword = "welcome"
        val isLoggedIn:Boolean
        super.onCreate(savedInstanceState)
        sharedPreferences =
            getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)
        isLoggedIn=sharedPreferences.getBoolean("isLoggedIn",false)
        if(isLoggedIn)
        {
            val intent=Intent(this@LoginActivity, HomeScreen::class.java)
            startActivity(intent)
            finish()
        }
        setContentView(R.layout.activity_login)
        etMobileNumber = findViewById(R.id.etMobileNumber)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        txtForgotPassword = findViewById(R.id.txtForgotPassword)
        txtRegister = findViewById(R.id.txtRegister)
        btnLogin.setOnClickListener {
            mobileNumber = etMobileNumber.text.toString()
            password = etPassword.text.toString()
            if (mobileNumber == validMobileNumber && password == validPassword) {
                val bundle=Bundle()
                bundle.putString("data","Login")
                bundle.putString("mobile",mobileNumber)
                bundle.putString("password",password)
                val intent = Intent(this@LoginActivity, HomeScreen::class.java)
                intent.putExtra("details",bundle)
                startActivity(intent)
                sharedPreferences.edit().putBoolean("isLoggedIn",true).apply()
                finish()
            } else {
                Toast.makeText(this@LoginActivity, "Incorrect Credentials", Toast.LENGTH_LONG)
                    .show()
            }
        }
        txtForgotPassword.setOnClickListener {
            val intent = Intent(this@LoginActivity, ForgotPassword::class.java)
            startActivity(intent)
        }
        txtRegister.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}