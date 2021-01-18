package com.example.innocare

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.innocare.dao.FoodDao
import com.example.innocare.dao.UsersDetailsDao
import com.example.innocare.model.UsersDetails

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.android.synthetic.main.activity_take_a_food.*
import kotlinx.android.synthetic.main.activity_track_calorie.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.*

class TakeAFoodActivity : AppCompatActivity(){

    val db = FirebaseFirestore.getInstance()
    val foodCollections = db.collection("foods")
    val userCollections = db.collection("users")
    val auth = Firebase.auth

    var uid:String = ""
    var name: String = ""
    var weight: Int = 0
    var height: Int = 0
    var age: Int = 0
    var gender: String = ""
    var activity: String = ""

    var totalCalories: Int = 0
    var dailyCalories: Int = 0
    var weeklyCalories: Int = 0
    var breakfastCalories: Int = 0
    var lunchCalories: Int = 0
    var dinnerCalories: Int = 0


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_take_a_food)

        btn_takefood.setOnClickListener {
            addFoodToDB()
        }

       if(et_calories.text.toString().isNotEmpty()) {
           getOldUsersDetails()

           updateCalories(getNewUsersDetails())
       }

    }
    private fun addFoodToDB() {

        //Get text from editTexts
        val foodName = et_food_item.text.toString()
        val fats = et_fats.text.toString()
        val protein = et_protein.text.toString()
        val carbohydrates = et_carbohydrates.text.toString()
        val calories = et_calories.text.toString()
        val qty = et_quantity.text.toString()
        val type = intent.getStringExtra("EXTRA_TYPE")


        //Check that the form is complete before submitting data to the database
        if (!(foodName.isEmpty() || fats.isEmpty() ||
                        protein.isEmpty() || carbohydrates.isEmpty() || calories.isEmpty() || qty.isEmpty())) {

            val foodDao = FoodDao()
            foodDao.takeFood(foodName, fats.toInt(), protein.toInt(), carbohydrates.toInt(), calories.toInt(), qty.toInt(),
                    et_type.text.toString())

            Toast.makeText(this, "Food added successfully!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, AddFoodActivity::class.java).also{
                it.putExtra("EXTRA_TYPE",type)
                startActivity(it)
            }

        } else {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getOldUsersDetails() {
        val currentUserId = auth.currentUser!!.uid

        CoroutineScope(Dispatchers.IO).launch {
            try{
                val userDetailsDao = UsersDetailsDao()
                val user = userDetailsDao.getUserById(currentUserId).await().toObject(UsersDetails::class.java)

                withContext(Dispatchers.Main){

                    uid = user!!.uid
                    name = user.name
                    weight = user.weight
                    height = user.height
                    age = user.age
                    gender = user.gender
                    activity = user.activity

                    totalCalories = user.totalCalories
                    dailyCalories = user.dailyCalories
                    weeklyCalories = user.weeklyCalories
                    breakfastCalories = user.breakfastCalories
                    lunchCalories = user.lunchCalories
                    dinnerCalories = user.dinnerCalories
                }
            } catch (e: Exception) {
                Log.d("TAG", "takeFood: ${e.message}")
            }
        }
    }

    private fun getNewUsersDetails(): Map<String, Any> {
        val map = mutableMapOf<String, Any>()
        when(et_type.text.toString()) {
            "Breakfast" -> {
                map["breakfastCalories"] = breakfastCalories + et_calories.text.toString().toInt()
            }

            "lunch" -> {
                map["lunchCalories"] = lunchCalories + et_calories.text.toString().toInt()

            }

            "Dinner" -> {
                map["dinnerCalories"] = dinnerCalories + et_calories.text.toString().toInt()

            }
        }

        map["totalCalories"] = totalCalories + et_calories.text.toString().toInt()
        map["dailyCalories"] = dailyCalories + et_calories.text.toString().toInt()
        map["weeklyCalories"] = weeklyCalories + et_calories.text.toString().toInt()

        return map
    }

    private fun updateCalories(newUsersDetailsMap: Map<String, Any>) = CoroutineScope(Dispatchers.IO).launch {
        val usersDetailsQuery = userCollections
                .get().await()

        if(usersDetailsQuery.documents.isNotEmpty()) {
            for(document in usersDetailsQuery) {
                try{
                    userCollections.document(document.id).set(
                            newUsersDetailsMap,
                            SetOptions.merge()
                    )
                } catch (e: Exception){
                    withContext(Dispatchers.Main){
                        Toast.makeText(this@TakeAFoodActivity, e.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }else{
            withContext(Dispatchers.Main){
                Toast.makeText(this@TakeAFoodActivity, "Empty Users ", Toast.LENGTH_LONG).show()
            }
        }
    }

}