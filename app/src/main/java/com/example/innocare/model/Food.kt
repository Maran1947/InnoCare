package com.example.innocare.model

data class Food(
        var foodName: String = "",
        var fats: Int = 0,
        var protein: Int = 0,
        var carbohydrates: Int = 0,
        var calories: Int = 0,
        var qty: Int = 0,
        var createdBy: String = "",
        var createdAt: Long = 0,
        var type: String = ""
        )