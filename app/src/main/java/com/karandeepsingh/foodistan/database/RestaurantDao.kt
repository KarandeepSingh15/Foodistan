package com.karandeepsingh.foodistan.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RestaurantDao {
    @Insert
    fun insert(restaurantEntity:RestaurantEntity)
    @Delete
    fun delete(restaurantEntity: RestaurantEntity)
    @Query("Select * from restaurant")
    fun getAllRestaurants():List<RestaurantEntity>
    @Query("Select * from restaurant where Id=:bookId")
    fun getRestaurantById(bookId:Int):RestaurantEntity

}