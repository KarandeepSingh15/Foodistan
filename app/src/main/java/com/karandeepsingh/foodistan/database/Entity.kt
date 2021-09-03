package com.karandeepsingh.foodistan.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "restaurant")
data class RestaurantEntity(
    @PrimaryKey var Id:Int,
    @ColumnInfo(name = "restaurant_name")var restaurantName: String,
    @ColumnInfo(name="rating") var rating:String,
    @ColumnInfo(name="cost_for_one") var pricePerPerson:String,
    @ColumnInfo(name="restaurant_image") var restaurantImage: String
)