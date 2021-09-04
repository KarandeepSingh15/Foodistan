package com.karandeepsingh.foodistan.fragments

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.karandeepsingh.foodistan.R
import com.karandeepsingh.foodistan.adapter.RestaurantAdapter
import com.karandeepsingh.foodistan.database.RestaurantDatabase
import com.karandeepsingh.foodistan.database.RestaurantEntity
import com.karandeepsingh.foodistan.model.Restaurant


class Favourites : Fragment() {
    lateinit var recyclerFavourites:RecyclerView
    lateinit var recyclerLayoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter:RestaurantAdapter
    lateinit var ProgressLayout:RelativeLayout
    lateinit var rlIsEmpty:RelativeLayout
    var favouritesList= arrayListOf<Restaurant>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_favourites, container, false)
        recyclerFavourites=view.findViewById(R.id.recyclerFavourites)
        recyclerLayoutManager=LinearLayoutManager(activity as Context)
        ProgressLayout=view.findViewById(R.id.ProgressLayout)
        ProgressLayout.visibility=View.VISIBLE
        recyclerFavourites.layoutManager=recyclerLayoutManager
        rlIsEmpty=view.findViewById(R.id.rlIsEmpty)
        rlIsEmpty.visibility=View.GONE
        if(activity!=null)
        {
            ProgressLayout.visibility=View.GONE
            val list=DBAsync(activity as Context).execute().get()
            if(list.isEmpty())
            {

                rlIsEmpty.visibility=View.VISIBLE
            }
            for(element in list)
            {
                val restaurant=Restaurant(
                    element.Id.toString(),
                    element.restaurantName,
                    element.rating,
                    element.pricePerPerson,
                    element.restaurantImage
                )
                favouritesList.add(restaurant)
            }
            recyclerAdapter=
                RestaurantAdapter(activity as Context,favouritesList)
            recyclerFavourites.adapter=recyclerAdapter

        }

        return view
    }


class DBAsync(val context: Context): AsyncTask<Void,Void,List<RestaurantEntity>>()
{
    val db= Room.databaseBuilder(context,RestaurantDatabase::class.java,"restaurant_db").build()
    override fun doInBackground(vararg params: Void?): List<RestaurantEntity> {
       return db.restaurantDao().getAllRestaurants()
    }

}

}