package com.karandeepsingh.foodistan.model

import org.json.JSONArray

data class OrderHistoryItem(
    val orderId:String,
    val restaurantName:String,
    val totalCost:String,
    val orderPlacedAt:String,
    val foodItems:JSONArray
)