package com.karandeepsingh.foodistan.fragments

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.karandeepsingh.foodistan.APIKEY
import com.karandeepsingh.foodistan.R
import com.karandeepsingh.foodistan.adapter.RestaurantAdapter
import com.karandeepsingh.foodistan.model.Restaurant
import com.karandeepsingh.foodistan.util.ConnectionManager
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.Comparator
import kotlin.collections.HashMap


class HomePage : Fragment() {
    lateinit var rvRestaurantList: RecyclerView
    lateinit var ProgressLayout: RelativeLayout
    lateinit var ProgressBar: ProgressBar
    lateinit var RecyclerLayoutManager: RecyclerView.LayoutManager
    lateinit var RecyclerAdapter: RestaurantAdapter
    var restaurantList = arrayListOf<Restaurant>()
    val compareCost= Comparator<Restaurant>{restaurant1,restaurant2->
        if(restaurant1.pricePerPerson.compareTo(restaurant2.pricePerPerson)==0)
        {
            restaurant1.restaurantName.compareTo(restaurant2.restaurantName)
        }
        else
        {
            restaurant1.pricePerPerson.compareTo(restaurant2.pricePerPerson)
        }

}
    val compareRating= Comparator<Restaurant>{restaurant1,restaurant2->
        if(restaurant1.rating.compareTo(restaurant2.rating)==0)
        {
            restaurant1.restaurantName.compareTo(restaurant2.restaurantName)
        }
        else
        {
            restaurant1.rating.compareTo(restaurant2.rating)
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home_page, container, false)
        rvRestaurantList = view.findViewById(R.id.rvRestaurantList)
        ProgressLayout = view.findViewById(R.id.ProgressLayout)
        ProgressLayout.visibility = View.VISIBLE
        ProgressBar = view.findViewById(R.id.ProgressBar)
        RecyclerLayoutManager = LinearLayoutManager(activity as Context)


        if (ConnectionManager().checkConnectivity(activity as Context)) {

            ProgressLayout.visibility = View.GONE
            val queue = Volley.newRequestQueue(activity as Context)
            val url = "http://13.235.250.119/v2/restaurants/fetch_result/"
            val jsonObjectRequest = object : JsonObjectRequest(Request.Method.GET, url, null,
                Response.Listener {
                    try {

                        val data = it.getJSONObject("data")
                        val success = data.getBoolean("success")
                        if (success) {
                            val jsonArray = data.getJSONArray("data")
                            for (i in 0 until jsonArray.length()) {
                                val restaurantObject = jsonArray.getJSONObject(i)
                                val restaurant = Restaurant(
                                    restaurantObject.getString("id"),
                                    restaurantObject.getString("name"),
                                    restaurantObject.getString("rating"),
                                    restaurantObject.getString("cost_for_one"),
                                    restaurantObject.getString("image_url")
                                )
                                restaurantList.add(restaurant)
                            }
                            if(activity!=null){
                            RecyclerAdapter = RestaurantAdapter(activity as Context, restaurantList)
                            rvRestaurantList.layoutManager = RecyclerLayoutManager
                            rvRestaurantList.adapter = RecyclerAdapter
                        } }else {
                            if (activity != null)
                                Toast.makeText(
                                    activity,
                                    "Error in receiving data!!",
                                    Toast.LENGTH_LONG
                                ).show()

                        }
//
                    } catch (e: JSONException) {
                        if (activity != null)
                            Toast.makeText(activity, "Some Unexpected Error!!", Toast.LENGTH_LONG)
                                .show()
                    }


                },
                Response.ErrorListener {
                    if (activity != null) {
                        println(it)
                        Toast.makeText(activity, "Volley Error($it) Occurred!!", Toast.LENGTH_LONG)
                            .show()
                    }

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
            alertDialog.create()
            alertDialog.show()
        }
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.filter_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.Sort) {
            val sort = arrayOf("Cost(Low To High)", "Cost(High To Low)", "Rating")
            var selectedItem = 0
            val alertDialog = AlertDialog.Builder(activity as Context)
            alertDialog.setTitle("Sort By?")
            alertDialog.setSingleChoiceItems(sort, selectedItem) { dialog, which ->
                selectedItem = which
            }
            alertDialog.setPositiveButton("Sort") { text, Listener ->
                when (selectedItem) {
                    0 -> {
                        Collections.sort(restaurantList,compareCost)

                    }
                    1 -> {
                        Collections.sort(restaurantList,compareCost)
                        restaurantList.reverse()
                    }
                    2 -> {
                        Collections.sort(restaurantList,compareRating)
                        restaurantList.reverse()
                    }
                }
                RecyclerAdapter.notifyDataSetChanged()
            }
            alertDialog.setNegativeButton("Cancel"){text,Listener->

            }
            alertDialog.create()
            alertDialog.show()
        }
        return super.onOptionsItemSelected(item)
    }

}