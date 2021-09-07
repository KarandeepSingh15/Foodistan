package com.karandeepsingh.foodistan.activity

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.AlertDialog
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
import com.karandeepsingh.foodistan.adapter.MenuAdapter
import com.karandeepsingh.foodistan.database.OrderDatabase
import com.karandeepsingh.foodistan.database.OrderEntity
import com.karandeepsingh.foodistan.database.RestaurantDatabase
import com.karandeepsingh.foodistan.model.FoodItem
import com.karandeepsingh.foodistan.util.ConnectionManager
import org.json.JSONException

class RestaurantDetails : AppCompatActivity() {
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var recyclerMenu: RecyclerView
    lateinit var btnProceed: Button
    lateinit var rlProgressLayout: RelativeLayout
    lateinit var menuLayoutManager: RecyclerView.LayoutManager
    lateinit var menuAdapter: MenuAdapter
    val orderList= arrayListOf<FoodItem>()
    val foodList = arrayListOf<FoodItem>()
    var restaurant_id:String?=null
    var restaurant_name:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_details)
        toolbar = findViewById(R.id.toolbar)
        recyclerMenu = findViewById(R.id.recyclerMenu)
        rlProgressLayout = findViewById(R.id.rlProgressLayout)
        rlProgressLayout.visibility = View.VISIBLE
        btnProceed = findViewById(R.id.btnProceed)
        btnProceed.visibility = View.GONE
        menuLayoutManager = LinearLayoutManager(this@RestaurantDetails)

        recyclerMenu.layoutManager = menuLayoutManager
        if(intent!=null)
        {
            restaurant_id=intent.getBundleExtra("restaurant_details")?.getString("restaurant_id")
            restaurant_name=intent.getBundleExtra("restaurant_details")?.getString("restaurant_name")
        }
            setToolbar()

        if (ConnectionManager().checkConnectivity(this@RestaurantDetails)) {
            if(restaurant_id!=null)
            {
                rlProgressLayout.visibility = View.GONE
                val queue = Volley.newRequestQueue(this@RestaurantDetails)
                val url = "http://13.235.250.119/v2/restaurants/fetch_result/$restaurant_id"
                val jsonObjectRequest =
                    object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {
                        try {
                            val data = it.getJSONObject("data")
                            if (data.getBoolean("success")) {
                                val jsonArray=data.getJSONArray("data")
                                for(i in 0 until jsonArray.length())
                                {
                                    val jsonObject=jsonArray.getJSONObject(i)
                                    val foodItem=FoodItem(jsonObject.getString("id"),
                                        jsonObject.getString("name"),
                                        jsonObject.getString("cost_for_one"),
                                        jsonObject.getString("restaurant_id"))
                                    foodList.add(foodItem)
                                }
                                menuAdapter=MenuAdapter(this@RestaurantDetails as Context,foodList,
                                object:MenuAdapter.OnItemClickListener{
                                    override fun onAddItem(foodItem: FoodItem) {
                                        btnProceed.visibility=View.VISIBLE
                                        orderList.add(foodItem)
                                    }

                                    override fun onRemoveItem(foodItem: FoodItem) {
                                        orderList.remove(foodItem)
                                        if(orderList.size==0)
                                            btnProceed.visibility=View.GONE
                                    }
                                })
                                recyclerMenu.adapter=menuAdapter
                            } else {
                                Toast.makeText(
                                    this@RestaurantDetails,
                                    "Some Unexpected Error Occurred!!",
                                    Toast.LENGTH_LONG
                                ).show()
                            }

                        } catch (e: JSONException) {
                            Toast.makeText(
                                this@RestaurantDetails,
                                "Some Unexpected Error Occurred!!",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    },
                        Response.ErrorListener {
                            Toast.makeText(
                                this@RestaurantDetails,
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
            }



        } else {

            val alertDialog = AlertDialog.Builder(this@RestaurantDetails)
            alertDialog.setTitle("Failure")
            alertDialog.setMessage("Internet Connection Not Found")
            alertDialog.setPositiveButton("Open Settings") { text, Listener ->
                startActivity(Intent(Settings.ACTION_SETTINGS))
                finish()
            }
            alertDialog.setNegativeButton("Exit") { text, Listener ->
                ActivityCompat.finishAffinity(this@RestaurantDetails)
            }
            alertDialog.create()
            alertDialog.show()
        }
        btnProceed.setOnClickListener {
            val gson= Gson()
            val foodItems=gson.toJson(orderList)
            val orderEntity=OrderEntity(restaurant_id as String,restaurant_name as String,foodItems)
            val DeleteDBTask=DBAsync(this@RestaurantDetails,null,3,restaurant_name as String).execute()
            val AsyncTask=DBAsync(this@RestaurantDetails,orderEntity,2,restaurant_name as String).execute()
            if(AsyncTask.get())
            {
                startActivity(Intent(this@RestaurantDetails,Cart::class.java))
                finish()
            }


        }

    }
    class DBAsync(val context: Context,val orderEntity: OrderEntity?,val mode:Int,val restaurantName:String):AsyncTask<Void,Void,Boolean>()
    {
        val db= Room.databaseBuilder(context,OrderDatabase::class.java,"order_details").build()
        override fun doInBackground(vararg params: Void?): Boolean {
            when(mode)
            {
                1->{
                    //Checking
                    db.orderDao().getOrderByRestaurantName(restaurantName)
                    db.close()
                    return true
                }
                2-> {//Adding order
                    db.orderDao().insertOrder(orderEntity as OrderEntity)
                    db.close()
                    return true
                }
                3->{
                    //Removing all orders
                    db.orderDao().deleteAllOrder()
                    db.close()
                    return true
                }
            }

            return false

        }

    }

    override fun onBackPressed() {
        if(DBAsync(this@RestaurantDetails,null,1,restaurant_name as String).execute().get())
        {
            val AsyncDB=DBAsync(this@RestaurantDetails,null,3,restaurant_name as String).execute()
            println(AsyncDB.get())
        }
        super.onBackPressed()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    fun setToolbar()
    {
        setSupportActionBar(toolbar)
        supportActionBar?.title=restaurant_name
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}