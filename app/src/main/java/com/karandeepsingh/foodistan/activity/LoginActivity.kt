package com.karandeepsingh.foodistan.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.karandeepsingh.foodistan.APIKEY
import com.karandeepsingh.foodistan.R
import com.karandeepsingh.foodistan.util.ConnectionManager
import org.json.JSONException
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    lateinit var etMobileNumber: EditText
    lateinit var etPassword: EditText
    lateinit var btnLogin: Button
    lateinit var txtForgotPassword: TextView
    lateinit var txtRegister: TextView
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {


        val isLoggedIn: Boolean
        super.onCreate(savedInstanceState)
        sharedPreferences =
            getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)
        isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        if (isLoggedIn) {
            val intent = Intent(this@LoginActivity, HomeScreen::class.java)
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
            if (ConnectionManager().checkConnectivity(this@LoginActivity)) {
                val queue = Volley.newRequestQueue(this@LoginActivity)
                val url = "http://13.235.250.119/v2/login/fetch_result"
                val jsonParams = JSONObject()
                jsonParams.put("mobile_number", etMobileNumber.text.toString())
                jsonParams.put("password", etPassword.text.toString())
                val jsonObjectRequest =
                    object : JsonObjectRequest(Request.Method.POST, url, jsonParams,
                        Response.Listener {
                            try {
                                val responseObject = it.getJSONObject("data")
                                if (responseObject.getBoolean("success")) {
                                    val data=responseObject.getJSONObject("data")
                                    sharedPreferences.edit().putString("user_id",data.getString("user_id")).apply()
                                    sharedPreferences.edit().putString("name",data.getString("name")).apply()
                                    sharedPreferences.edit().putString("email",data.getString("email")).apply()
                                    sharedPreferences.edit().putString("mobile_number",data.getString("mobile_number")).apply()
                                    sharedPreferences.edit().putString("address",data.getString("address")).apply()
                                    startActivity(Intent(this@LoginActivity, HomeScreen::class.java))
                                    sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()
                                    finish()
                                } else {
                                    Toast.makeText(
                                        this@LoginActivity,
                                        "Incorrect Credentials",
                                        Toast.LENGTH_LONG
                                    )
                                        .show()
                                }

                            }catch (e:JSONException)
                            {
                                Toast.makeText(
                                    this@LoginActivity,
                                    "Some Unexpected Error!!",
                                    Toast.LENGTH_LONG
                                )
                                    .show()
                            }

                        },
                        Response.ErrorListener {
                            Toast.makeText(
                                this@LoginActivity,
                                "Volley Error($it) Occurred!!",
                                Toast.LENGTH_LONG
                            ).show()
                        }) {
                        override fun getHeaders(): MutableMap<String, String> {
                            val headers = HashMap<String, String>()
                            headers["Content-type"] = "application/json"
                            headers["Token"] = APIKEY().token
                            return headers
                        }
                    }
                queue.add(jsonObjectRequest)

            } else {
                val alertDialog = AlertDialog.Builder(this@LoginActivity)
                alertDialog.setTitle("Failure")
                alertDialog.setMessage("Internet Connection Not Found")
                alertDialog.setPositiveButton("Open Settings") { text, Listener ->
                    startActivity(Intent(Settings.ACTION_SETTINGS))
                    finish()
                }
                alertDialog.setNegativeButton("Exit") { text, Listener ->
                    ActivityCompat.finishAffinity(this@LoginActivity)
                }
                alertDialog.create()
                alertDialog.show()
            }

        }
        txtForgotPassword.setOnClickListener {
            val intent = Intent(this@LoginActivity, ForgotPassword::class.java)
            startActivity(intent)
            finish()

        }
        txtRegister.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}