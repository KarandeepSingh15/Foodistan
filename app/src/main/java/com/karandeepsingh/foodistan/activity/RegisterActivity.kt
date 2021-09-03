package com.karandeepsingh.foodistan.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.karandeepsingh.foodistan.R

class RegisterActivity : AppCompatActivity() {
    lateinit var etName: EditText
    lateinit var etEmail: EditText
    lateinit var etMobileNumberRegister: EditText
    lateinit var etAddress: EditText
    lateinit var etPasswordRegister: EditText
    lateinit var etConfirmPassword: EditText
    lateinit var btnRegister: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        var password: String
        var confirmPassword: String
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etMobileNumberRegister = findViewById(R.id.etMobileNumberRegister)
        etAddress = findViewById(R.id.etAddress)
        etPasswordRegister = findViewById(R.id.etPasswordRegister)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        btnRegister = findViewById(R.id.btnRegister)
        btnRegister.setOnClickListener {
            password = etPasswordRegister.text.toString()
            confirmPassword = etConfirmPassword.text.toString()
            if (password != confirmPassword) {
                Toast.makeText(this@RegisterActivity, "Passwords are not same!", Toast.LENGTH_LONG)
                    .show()
            } else {
                val bundle=Bundle()
                bundle.putString("data","Register")
                bundle.putString("name",etName.text.toString())
                bundle.putString("email",etEmail.text.toString())
                bundle.putString("mobile",etMobileNumberRegister.text.toString())
                bundle.putString("address",etAddress.text.toString())
                bundle.putString("password",password)

                val intent=Intent(this@RegisterActivity, HomeScreen::class.java)
                intent.putExtra("details",bundle)
                startActivity(intent)

            }

        }

    }
}