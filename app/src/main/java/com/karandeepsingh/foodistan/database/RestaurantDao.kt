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
@Dao
interface OrderDao {
    @Insert
    fun insertOrder(orderEntity: OrderEntity)
    @Delete
    fun deleteOrder(orderEntity: OrderEntity)
    @Query("delete from order_table")
    fun deleteAllOrder()
    @Query("select * from order_table")
    fun getOrder():List<OrderEntity>
    @Query("select * from order_table where restaurant_name=:restaurantName")
    fun getOrderByRestaurantName(restaurantName:String):OrderEntity


}