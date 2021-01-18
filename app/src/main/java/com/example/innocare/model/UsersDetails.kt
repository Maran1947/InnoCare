package com.example.innocare.model

data class UsersDetails(
    var uid:String = "",
    var name: String = "",
    var weight: Int = 0,
    var height: Int = 0,
    var age: Int = 0,
    var gender: String = "",
    var activity: String = "",
    var totalCalories: Int = 0,
    var dailyCalories: Int = 0,
    var weeklyCalories: Int = 0,
    var breakfastCalories: Int = 0,
    var lunchCalories: Int = 0,
    var dinnerCalories: Int = 0
)