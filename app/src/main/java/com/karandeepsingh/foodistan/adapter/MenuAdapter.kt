package com.karandeepsingh.foodistan.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.karandeepsingh.foodistan.R
import com.karandeepsingh.foodistan.model.FoodItem

class MenuAdapter(val context: Context,val foodlist:ArrayList<FoodItem>,val listener:OnItemClickListener):RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    interface OnItemClickListener{
        fun onAddItem(foodItem: FoodItem)
        fun onRemoveItem(foodItem: FoodItem)
    }
    class MenuViewHolder(view: View):RecyclerView.ViewHolder(view)
    {
        val txtItemNumber:TextView=view.findViewById(R.id.txtItemNumber)
        val txtItemName:TextView=view.findViewById(R.id.txtItemName)
        val txtItemPrice:TextView=view.findViewById(R.id.txtItemPrice)
        val btnAddOrRemove:Button=view.findViewById(R.id.btnAddOrRemove)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.single_menu_item,parent,false)
        return MenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val foodItem=foodlist[position]
        holder.txtItemNumber.text=(position+1).toString()
        holder.txtItemName.text=foodItem.name
        holder.txtItemPrice.text=foodItem.cost_for_one
        holder.btnAddOrRemove.setOnClickListener {
            if(foodItem !in foodItemList)
            {
                foodItemList.add(foodItem)
                listener.onAddItem(foodItem)
                val color=ContextCompat.getColor(context,R.color.red)
                holder.btnAddOrRemove.text="Remove"
                holder.btnAddOrRemove.setBackgroundColor(color)
            }
            else
            {
                foodItemList.remove(foodItem)
                listener.onRemoveItem(foodItem)
                val color=ContextCompat.getColor(context,R.color.primaryColor)
                holder.btnAddOrRemove.text="Add"
                holder.btnAddOrRemove.setBackgroundColor(color)
            }
        }

    }

    override fun getItemCount(): Int {
        return foodlist.size
    }
    var foodItemList= arrayListOf<FoodItem>()
}