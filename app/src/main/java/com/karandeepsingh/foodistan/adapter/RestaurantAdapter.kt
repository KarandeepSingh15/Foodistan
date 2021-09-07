package com.karandeepsingh.foodistan.adapter

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.karandeepsingh.foodistan.R
import com.karandeepsingh.foodistan.activity.RestaurantDetails
import com.karandeepsingh.foodistan.database.RestaurantDatabase
import com.karandeepsingh.foodistan.database.RestaurantEntity
import com.karandeepsingh.foodistan.model.Restaurant
import com.squareup.picasso.Picasso

class RestaurantAdapter(val context: Context, val restaurantList: ArrayList<Restaurant>) :
    RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder>() {

    class RestaurantViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var imgRestaurant: ImageView = view.findViewById(R.id.imgRestaurant)
        var txtRestaurantName: TextView = view.findViewById(R.id.txtRestaurantName)
        var txtPricePerPerson: TextView = view.findViewById(R.id.txtPricePerPerson)
        var txtFav: TextView = view.findViewById(R.id.txtFav)
        var txtRestaurantRating: TextView = view.findViewById(R.id.txtRestaurantRating)
        var singleRestaurantItem:CardView=view.findViewById(R.id.singleRestaurantItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.restaurant_single_item, parent, false)
        return RestaurantViewHolder(view)
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        val restaurant = restaurantList[position]
        Picasso.get().load(restaurant.restaurantImage).error(R.drawable.default_restaurant)
            .into(holder.imgRestaurant)
        holder.txtRestaurantName.text = restaurant.restaurantName
        holder.txtPricePerPerson.text = "₹${restaurant.pricePerPerson}/Person"
        holder.txtRestaurantRating.text = restaurant.rating
        val restaurantEntity=RestaurantEntity(
            restaurant.restaurantId.toInt() as Int,
            holder.txtRestaurantName.text.toString(),
            holder.txtRestaurantRating.text.toString(),
            holder.txtPricePerPerson.text.toString(),
            restaurant.restaurantImage
        )
        val checkFav = DBAsync(context,restaurantEntity,1).execute()
        val isFav:Boolean=checkFav.get()
        if(isFav)
        {
            holder.txtFav.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favourites,0,0,0)
        }
        else
        {
            holder.txtFav.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_favourites_empty,0,0,0,)
        }
        holder.txtFav.setOnClickListener {
            if(!DBAsync(context,restaurantEntity,1).execute().get())
            {
                val asyncTask=DBAsync(context,restaurantEntity,2).execute()
                val success=asyncTask.get()
                if(success)
                {
                    Toast.makeText(context,"Added to Favourites",Toast.LENGTH_SHORT).show()
                    holder.txtFav.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favourites,0,0,0)
                }
                else
                {
                    Toast.makeText(context,"Error Occurred while adding to Favourites",Toast.LENGTH_SHORT).show()
                }

            }
            else
            {
                val asyncTask=DBAsync(context,restaurantEntity,3).execute()
                val success=asyncTask.get()
                if(success)
                {
                    Toast.makeText(context,"Removed from Favourites",Toast.LENGTH_SHORT).show()
                    holder.txtFav.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_favourites_empty,0,0,0)
                }
                else
                {
                    Toast.makeText(context,"Error Occurred while removing from Favourites",Toast.LENGTH_SHORT).show()
                }
            }
        }
        holder.singleRestaurantItem.setOnClickListener {
            val bundle= Bundle()
            bundle.putString("restaurant_id",restaurant.restaurantId)
            bundle.putString("restaurant_name",restaurant.restaurantName)
            val intent=Intent(context,RestaurantDetails::class.java)
            intent.putExtra("restaurant_details",bundle)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return restaurantList.size
    }

    class DBAsync(val context: Context, val restaurantEntity: RestaurantEntity, val mode: Int) :
        AsyncTask<Void, Void, Boolean>() {
        val db =
            Room.databaseBuilder(context, RestaurantDatabase::class.java, "restaurant_db").build()

        override fun doInBackground(vararg params: Void?): Boolean {
            when (mode) {
                /*
                1-> Checking if available in favourites
                2-> Inserting to favourites
                3-> Deleting from favourites
                 */
                1 -> {
                    val restaurant: RestaurantEntity? =
                        db.restaurantDao().getRestaurantById(restaurantEntity.Id)
                    db.close()
                    return restaurant != null
                }
                2 -> {
                    db.restaurantDao().insert(restaurantEntity)
                    db.close()
                    return true
                }
                3 -> {
                    db.restaurantDao().delete(restaurantEntity)
                    db.close()
                    return true
                }

            }

            return false
        }

    }
}