package com.karandeepsingh.foodistan.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.json.JSONObject

@Entity(tableName = "restaurant")
data class RestaurantEntity(
    @PrimaryKey var Id:Int,
    @ColumnInfo(name = "restaurant_name")var restaurantName: String,
    @ColumnInfo(name="rating") var rating:String,
    @ColumnInfo(name="cost_for_one") var pricePerPerson:String,
    @ColumnInfo(name="restaurant_image") var restaurantImage: String
)
@Entity(tableName = "order_table")
data class OrderEntity(
    @PrimaryKey val restaurantId:String,
    @ColumnInfo(name = "restaurant_name")val restaurantName:String,
    @ColumnInfo(name = "order_details")val order:String
)