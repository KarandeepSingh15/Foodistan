package com.karandeepsingh.foodistan.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.karandeepsingh.foodistan.R
import com.karandeepsingh.foodistan.fragments.OrderHistory
import com.karandeepsingh.foodistan.model.FoodItem
import com.karandeepsingh.foodistan.model.OrderHistoryItem

class OrderHistoryAdapter(val context: Context, val orderHistoryList: List<OrderHistoryItem>) :
    RecyclerView.Adapter<OrderHistoryAdapter.OrderHistoryViewHolder>() {
    class OrderHistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtRestaurantName: TextView = view.findViewById(R.id.txtRestaurantName)
        val txtDate: TextView = view.findViewById(R.id.txtDate)
        val RecyclerFoodMenu: RecyclerView = view.findViewById(R.id.RecyclerFoodMenu)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.order_history_single_item, parent, false)
        return OrderHistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderHistoryViewHolder, position: Int) {
        val orderHistoryItem = orderHistoryList[position]
        holder.txtRestaurantName.text = orderHistoryItem.restaurantName
        holder.txtDate.text = orderHistoryItem.orderPlacedAt.subSequence(0, 8)
        val FoodItemList = arrayListOf<FoodItem>()
        for (i in 0 until orderHistoryItem.foodItems.length()) {
            val jsonObject = orderHistoryItem.foodItems.getJSONObject(i)
            val foodItem = FoodItem(
                jsonObject.getString("food_item_id"),
                jsonObject.getString("name"), jsonObject.getString("cost"), null
            )
            FoodItemList.add(foodItem)
        }
        val FoodMenuLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        val FoodMenuAdapter: CartAdapter = CartAdapter(context,FoodItemList)
        holder.RecyclerFoodMenu.layoutManager=FoodMenuLayoutManager
        holder.RecyclerFoodMenu.adapter=FoodMenuAdapter
    }

    override fun getItemCount(): Int {
        return orderHistoryList.size
    }
}