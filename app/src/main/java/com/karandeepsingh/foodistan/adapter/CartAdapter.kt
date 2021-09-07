package com.karandeepsingh.foodistan.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.karandeepsingh.foodistan.R
import com.karandeepsingh.foodistan.model.FoodItem

class CartAdapter(val context:Context,val orderList:ArrayList<FoodItem>):RecyclerView.Adapter<CartAdapter.CartViewHolder>() {
    class CartViewHolder(view: View):RecyclerView.ViewHolder(view)
    {
        val txtItemName:TextView=view.findViewById(R.id.txtItemName)
        val txtItemPrice:TextView=view.findViewById(R.id.txtItemPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.cart_single_item,parent,false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val orderItem:FoodItem=orderList[position]
        holder.txtItemName.text=orderItem.name
        holder.txtItemPrice.text=orderItem.cost_for_one

    }

    override fun getItemCount(): Int {
        return orderList.size
    }
}