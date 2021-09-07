package com.karandeepsingh.foodistan.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.karandeepsingh.foodistan.APIKEY
import com.karandeepsingh.foodistan.R
import com.karandeepsingh.foodistan.adapter.CartAdapter
import com.karandeepsingh.foodistan.database.OrderDatabase
import com.karandeepsingh.foodistan.database.OrderEntity
import com.karandeepsingh.foodistan.model.FoodItem
import com.karandeepsingh.foodistan.util.ConnectionManager
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class Cart : AppCompatActivity() {
    lateinit var toolbar: Toolbar
    lateinit var llRestaurantDetails: LinearLayout
    lateinit var txtRestaurantName: TextView
    lateinit var RecyclerCart: RecyclerView
    lateinit var btnPlaceOrder: Button
    lateinit var rlPlaceOrder: RelativeLayout
    lateinit var btnOK: Button
    lateinit var cartLayoutManager: RecyclerView.LayoutManager
    lateinit var cartAdapter: CartAdapter
    lateinit var sharedPreferences: SharedPreferences
    var orderList = arrayListOf<FoodItem>()
    var restaurant_id: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        init()
        setupToolbar()
        cartLayoutManager = LinearLayoutManager(this@Cart)
        RecyclerCart.layoutManager = cartLayoutManager
        val orderEntity: List<OrderEntity> = DBAsyncTask(this@Cart).execute().get()
        if (orderEntity != null) {
            txtRestaurantName.text = orderEntity[0].restaurantName
            restaurant_id = orderEntity[0].restaurantId
            val foodItems = orderEntity[0].order
            orderList.addAll(
                Gson().fromJson(foodItems, Array<FoodItem>::class.java).asList()
            )
            cartAdapter = CartAdapter(this@Cart, orderList)
            RecyclerCart.adapter = cartAdapter
            var sum = 0
            val jsonArray = JSONArray()
            for (orderItem in orderList) {
                sum += orderItem.cost_for_one.toInt()
                val jsonObject = JSONObject()
                jsonObject.put("food_item_id", orderItem.id)
                jsonArray.put(jsonObject)
            }
            btnPlaceOrder.text = "Place Order(Total:Rs.$sum)"
            btnPlaceOrder.setOnClickListener {
                if (ConnectionManager().checkConnectivity(this@Cart)) {
                    val queue = Volley.newRequestQueue(this@Cart)
                    val url = "http://13.235.250.119/v2/place_order/fetch_result/"
                    val jsonParams = JSONObject()
                    jsonParams.put("user_id", getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE).getString("user_id",null))
                    jsonParams.put("restaurant_id", restaurant_id)
                    jsonParams.put("total_cost", sum)
                    jsonParams.put("food", jsonArray)
                    val jsonObjectRequest =
                        object : JsonObjectRequest(Request.Method.POST, url, jsonParams,
                            Response.Listener {
                                try {
                                    val data=it.getJSONObject("data")
                                    if(data.getBoolean("success"))
                                    {
                                        rlPlaceOrder.visibility=View.VISIBLE
                                        btnPlaceOrder.visibility=View.GONE
                                        btnOK.visibility=View.VISIBLE
                                        btnOK.setOnClickListener {
                                            DeleteDB(this@Cart).execute()
                                            val intent=Intent(this@Cart,HomeScreen::class.java)
                                            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                                            finish()
                                            startActivity(intent)
                                        }
                                    }
                                    else
                                    {
                                        Toast.makeText(
                                            this@Cart,
                                            "Some Unexpected Error Occurred!!",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }

                                } catch (e: JSONException) {
                                    Toast.makeText(
                                        this@Cart,
                                        "Some Unexpected Error Occurred!!",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }, Response.ErrorListener {
                                Toast.makeText(
                                    this,
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
                    val alertDialog = AlertDialog.Builder(this@Cart)
                    alertDialog.setTitle("Failure")
                    alertDialog.setMessage("No Internet Connection Found")
                    alertDialog.setPositiveButton("Open Settings") { text, Listener ->
                        startActivity(Intent(Settings.ACTION_SETTINGS))
                        finish()
                    }
                    alertDialog.setNegativeButton("Exit") { text, Listener ->
                        ActivityCompat.finishAffinity(this)
                    }
                }
            }
        } else {
            Toast.makeText(this@Cart, "Some Unexpected Error Occurred", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun init() {
        toolbar = findViewById(R.id.toolbar)
        llRestaurantDetails = findViewById(R.id.llRestaurantDetails)
        txtRestaurantName = findViewById(R.id.txtRestaurantName)
        RecyclerCart = findViewById(R.id.RecyclerCart)
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder)
        rlPlaceOrder = findViewById(R.id.rlPlaceOrder)
        rlPlaceOrder.visibility = View.GONE
        btnOK = findViewById(R.id.btnOK)
    }

    fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "My Cart"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onBackPressed() {
        startActivity(Intent(this@Cart,RestaurantDetails::class.java))

    }

    class DBAsyncTask(val context: Context) : AsyncTask<Void, Void, List<OrderEntity>>() {
        val db = Room.databaseBuilder(context, OrderDatabase::class.java, "order_details").build()
        override fun doInBackground(vararg params: Void?): List<OrderEntity> {
            return db.orderDao().getOrder()

        }

    }
    class DeleteDB(val context: Context):AsyncTask<Void,Void,Boolean>()
    {
        val db=Room.databaseBuilder(context,OrderDatabase::class.java,"order_details").build()
        override fun doInBackground(vararg params: Void?): Boolean {
            db.orderDao().deleteAllOrder()
            return true
        }

    }
}