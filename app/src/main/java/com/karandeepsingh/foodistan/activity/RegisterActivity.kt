package com.karandeepsingh.foodistan.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
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
import java.lang.reflect.Method

class RegisterActivity : AppCompatActivity() {
    lateinit var etName: EditText
    lateinit var etEmail: EditText
    lateinit var etMobileNumberRegister: EditText
    lateinit var etAddress: EditText
    lateinit var etPasswordRegister: EditText
    lateinit var etConfirmPassword: EditText
    lateinit var btnRegister: Button
    lateinit var sharedPreferences: SharedPreferences
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
                if (ConnectionManager().checkConnectivity(this@RegisterActivity)) {
                    val queue = Volley.newRequestQueue(this@RegisterActivity)
                    val url = "http://13.235.250.119/v2/register/fetch_result"
                    val jsonParams = JSONObject()
                    jsonParams.put("name", etName.text.toString())
                    jsonParams.put("mobile_number", etMobileNumberRegister.text.toString())
                    jsonParams.put("password", password)
                    jsonParams.put("address", etAddress.text.toString())
                    jsonParams.put("email", etEmail.text.toString())
                    val jsonObjectRequest =
                        object : JsonObjectRequest(Request.Method.POST, url, jsonParams,
                            Response.Listener {
                                try{
                                    val data=it.getJSONObject("data")
                                    if(data.getBoolean("success"))
                                    {
                                        sharedPreferences=getSharedPreferences(getString(R.string.preference_file_name),
                                            Context.MODE_PRIVATE)
                                        val responseData=data.getJSONObject("data")
                                        sharedPreferences.edit().putString("user_id",responseData.getString("user_id")).apply()
                                        sharedPreferences.edit().putString("name",responseData.getString("name")).apply()
                                        sharedPreferences.edit().putString("email",responseData.getString("email")).apply()
                                        sharedPreferences.edit().putString("mobile_number",responseData.getString("mobile_number")).apply()
                                        sharedPreferences.edit().putString("address",responseData.getString("address")).apply()
                                        startActivity(Intent(this@RegisterActivity,HomeScreen::class.java))
                                        finish()


                                    }
                                    else{
                                        Toast.makeText(this@RegisterActivity,"Some Unexpected Error Occurred!!",Toast.LENGTH_LONG).show()
                                    }

                                }catch (e:JSONException)
                                {
                                    Toast.makeText(this@RegisterActivity,"Some Unexpected Error Occurred!!",Toast.LENGTH_LONG).show()
                                }

                            },
                            Response.ErrorListener {
                                Toast.makeText(this@RegisterActivity,"Volley Error($it) Occurred!!",Toast.LENGTH_LONG).show()
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
                    val alertDialog = AlertDialog.Builder(this@RegisterActivity)
                    alertDialog.setTitle("Failure")
                    alertDialog.setMessage("Internet Connection Not Found")
                    alertDialog.setPositiveButton("Open Settings") { text, Listener ->
                        startActivity(Intent(Settings.ACTION_SETTINGS))
                        finish()
                    }
                    alertDialog.setNegativeButton("Exit") { text, Listener ->
                        ActivityCompat.finishAffinity(this@RegisterActivity)
                    }
                    alertDialog.create()
                    alertDialog.show()
                }

            }

        }

    }
}