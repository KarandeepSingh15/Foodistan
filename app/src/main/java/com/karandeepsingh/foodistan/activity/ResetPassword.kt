package com.karandeepsingh.foodistan.activity

import android.content.Intent
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

class ResetPassword : AppCompatActivity() {
    lateinit var etOTP: EditText
    lateinit var etNewPassword: EditText
    lateinit var etConfirmPassword: EditText
    lateinit var btnSubmit:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)
        var mobile_number: String? = null
        etOTP = findViewById(R.id.etOTP)
        etNewPassword = findViewById(R.id.etNewPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        btnSubmit=findViewById(R.id.btnSubmit)
        if (intent != null) {
            mobile_number = intent.getBundleExtra("bundle")?.getString("mobile_number")
        }
        btnSubmit.setOnClickListener {
            if (etNewPassword.text.toString() != etConfirmPassword.text.toString()) {
                Toast.makeText(this@ResetPassword, "Passwords Don't match!!", Toast.LENGTH_SHORT).show()
            } else {
                if (ConnectionManager().checkConnectivity(this@ResetPassword)) {
                    val queue = Volley.newRequestQueue(this@ResetPassword)
                    val url = "http://13.235.250.119/v2/reset_password/fetch_result"
                    val jsonParams = JSONObject()
                    jsonParams.put("mobile_number", mobile_number)
                    jsonParams.put("password", etNewPassword.text.toString())
                    jsonParams.put("otp", etOTP.text.toString())
                    val jsonObjectRequest =
                        object : JsonObjectRequest(Request.Method.POST, url, jsonParams,
                            Response.Listener {
                                try {
                                    val data=it.getJSONObject("data")
                                    if(data.getBoolean("success"))
                                    {
                                        Toast.makeText(
                                            this@ResetPassword,
                                            data.getString("successMessage"),
                                            Toast.LENGTH_LONG
                                        ).show()
                                        startActivity(Intent(this@ResetPassword,LoginActivity::class.java))
                                        finish()

                                    }
                                    else
                                    {
                                        Toast.makeText(
                                            this@ResetPassword,
                                            "Some Unexpected Error Occurred!!",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                } catch (e: JSONException) {
                                    Toast.makeText(
                                        this@ResetPassword,
                                        "Some Unexpected Error Occurred!!",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }

                            }, Response.ErrorListener {
                                Toast.makeText(
                                    this@ResetPassword,
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
                    val alertDialog = AlertDialog.Builder(this@ResetPassword)
                    alertDialog.setTitle("Failure")
                    alertDialog.setMessage("Internet Connection Not Found")
                    alertDialog.setPositiveButton("Open Settings") { text, Listener ->
                        startActivity(Intent(Settings.ACTION_SETTINGS))
                        finish()
                    }
                    alertDialog.setNegativeButton("Exit") { text, Listener ->
                        ActivityCompat.finishAffinity(this@ResetPassword)
                    }
                    alertDialog.create()
                    alertDialog.show()
                }

            }
        }


    }
}