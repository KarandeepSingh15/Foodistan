package com.karandeepsingh.foodistan.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.karandeepsingh.foodistan.APIKEY
import com.karandeepsingh.foodistan.R
import com.karandeepsingh.foodistan.adapter.OrderHistoryAdapter
import com.karandeepsingh.foodistan.model.OrderHistoryItem
import com.karandeepsingh.foodistan.util.ConnectionManager
import org.json.JSONException


class OrderHistory : Fragment() {
    lateinit var RecyclerPreviousOrder: RecyclerView
    lateinit var previousOrderLayoutManager: RecyclerView.LayoutManager
    lateinit var previousOrderAdapter: OrderHistoryAdapter
    val orderHistoryList = arrayListOf<OrderHistoryItem>()
    lateinit var sharedPreferences: SharedPreferences
    lateinit var ProgressLayout:RelativeLayout
    lateinit var rlIsEmpty:RelativeLayout


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_order_history, container, false)
        RecyclerPreviousOrder = view.findViewById(R.id.RecyclerPreviousOrder)
        ProgressLayout=view.findViewById(R.id.ProgressLayout)
        ProgressLayout.visibility=View.VISIBLE
        rlIsEmpty=view.findViewById(R.id.rlIsEmpty)
        rlIsEmpty.visibility=View.GONE

        if (activity != null) {
            sharedPreferences=activity?.getSharedPreferences(getString(R.string.preference_file_name),Context.MODE_PRIVATE) as SharedPreferences
            if (ConnectionManager().checkConnectivity(activity as Context)) {
                ProgressLayout.visibility=View.GONE
                val queue = Volley.newRequestQueue(activity as Context)
                val url = "http://13.235.250.119/v2/orders/fetch_result/${sharedPreferences.getString("user_id","")}"
                val jsonObjectRequest = object : JsonObjectRequest(
                    Request.Method.GET,
                    url,
                    null,
                    Response.Listener {
                        try {
                            val jsonObject=it.getJSONObject("data")
                            if(jsonObject.getBoolean("success"))
                            {
                                val data=jsonObject.getJSONArray("data")
                                for (i in 0 until data.length())
                                {
                                    val dataObject=data.getJSONObject(i)
                                    val orderHistoryItem=OrderHistoryItem(
                                        dataObject.getString("order_id"),
                                        dataObject.getString("restaurant_name"),
                                        dataObject.getString("total_cost"),
                                        dataObject.getString("order_placed_at"),
                                        dataObject.getJSONArray("food_items")
                                    )
                                    orderHistoryList.add(orderHistoryItem)
                                }
                                if(orderHistoryList.size==0)
                                    rlIsEmpty.visibility=View.VISIBLE
                                previousOrderLayoutManager=LinearLayoutManager(activity as Context)
                                previousOrderAdapter=OrderHistoryAdapter(activity as Context,orderHistoryList)
                                RecyclerPreviousOrder.layoutManager=previousOrderLayoutManager
                                RecyclerPreviousOrder.adapter=previousOrderAdapter
                            }
                            else{
                                Toast.makeText(
                                    activity as Context,
                                    "Some Unexpected Error Occurred!!",
                                    Toast.LENGTH_LONG
                                ).show()


                            }


                        } catch (e: JSONException) {
                            Toast.makeText(
                                activity as Context,
                                "Some Unexpected Error Occurred!!",
                                Toast.LENGTH_LONG
                            ).show()

                        }
                    },
                    Response.ErrorListener {
                        Toast.makeText(
                            activity as Context,
                            "VolleyError($it) Occurred!!",
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
                val alertDialog = AlertDialog.Builder(activity as Context)
                alertDialog.setTitle("Failure")
                alertDialog.setMessage("No Internet Connection Found")
                alertDialog.setPositiveButton("Open Settings") { text, Listener ->
                    startActivity(Intent(Settings.ACTION_SETTINGS))
                    activity?.finish()
                }
                alertDialog.setNegativeButton("Exit") { text, Listener ->
                    ActivityCompat.finishAffinity(activity as Activity)
                }

            }


        }

        return view
    }

}