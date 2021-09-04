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

class ForgotPassword : AppCompatActivity() {
    lateinit var etMobileNumberForgot: EditText
    lateinit var etEmailForgot: EditText
    lateinit var btnNext: Button
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        etMobileNumberForgot = findViewById(R.id.etMobileNumberForgot)
        etEmailForgot = findViewById(R.id.etEmailForgot)
        btnNext = findViewById(R.id.btnNext)
        btnNext.setOnClickListener {
            if (ConnectionManager().checkConnectivity(this@ForgotPassword)) {
                val queue = Volley.newRequestQueue(this@ForgotPassword)
                val url = "http://13.235.250.119/v2/forgot_password/fetch_result"
                val jsonParams = JSONObject()
                jsonParams.put("mobile_number", etMobileNumberForgot.text.toString())
                jsonParams.put("email", etEmailForgot.text.toString())
                val jsonObjectRequest =
                    object : JsonObjectRequest(Request.Method.POST, url, jsonParams,
                        Response.Listener {
                           try {
                               val data=it.getJSONObject("data")
                               if(data.getBoolean("success"))
                               {
                                   if(data.getBoolean("first_try"))
                                   {
                                       Toast.makeText(
                                           this@ForgotPassword,
                                           "OTP has been sent to your email and is valid for 24 hours",
                                           Toast.LENGTH_LONG
                                       ).show()
                                   }
                                   else
                                   {
                                       Toast.makeText(
                                           this@ForgotPassword,
                                           "Use the previous OTP sent by mail.",
                                           Toast.LENGTH_LONG
                                       ).show()
                                   }
                                   val bundle=Bundle()
                                   bundle.putString("mobile_number",etMobileNumberForgot.text.toString())
                                   val intent=Intent(this@ForgotPassword,ResetPassword::class.java)
                                   intent.putExtra("bundle",bundle)
                                   startActivity(intent)
                                   finish()
                               }
                               else
                               {
                                   Toast.makeText(
                                       this@ForgotPassword,
                                       "Some Unexpected Error Occurred!!",
                                       Toast.LENGTH_LONG
                                   ).show()
                               }

                           }catch (e:JSONException)
                           {
                               Toast.makeText(
                                   this@ForgotPassword,
                                   "Some Unexpected Error Occurred!!",
                                   Toast.LENGTH_LONG
                               ).show()
                           }
                        }, Response.ErrorListener {
                            Toast.makeText(
                                this@ForgotPassword,
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
                val alertDialog = AlertDialog.Builder(this@ForgotPassword)
                alertDialog.setTitle("Failure")
                alertDialog.setMessage("Internet Connection Not Found")
                alertDialog.setPositiveButton("Open Settings") { text, Listener ->
                    startActivity(Intent(Settings.ACTION_SETTINGS))
                    finish()
                }
                alertDialog.setNegativeButton("Exit") { text, Listener ->
                    ActivityCompat.finishAffinity(this@ForgotPassword)
                }
                alertDialog.create()
                alertDialog.show()
            }
        }

    }

    override fun onBackPressed() {
        startActivity(Intent(this@ForgotPassword,LoginActivity::class.java))
        finish()
    }
}