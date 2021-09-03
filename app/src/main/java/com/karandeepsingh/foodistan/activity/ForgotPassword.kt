package com.karandeepsingh.foodistan.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.karandeepsingh.foodistan.R

class ForgotPassword : AppCompatActivity() {
    lateinit var etMobileNumberForgot:EditText
    lateinit var etEmailForgot:EditText
    lateinit var btnNext:Button
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        etMobileNumberForgot=findViewById(R.id.etMobileNumberForgot)
        etEmailForgot=findViewById(R.id.etEmailForgot)
        btnNext=findViewById(R.id.btnNext)
        btnNext.setOnClickListener {
            val bundle=Bundle()
            bundle.putString("data","Forgot")
            bundle.putString("mobile",etMobileNumberForgot.text.toString())
            bundle.putString("email",etEmailForgot.text.toString())
            val intent= Intent(this@ForgotPassword, HomeScreen::class.java)
            intent.putExtra("details",bundle)
            startActivity(intent)
        }
    }
}