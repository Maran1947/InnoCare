package com.example.innocare.dao

import android.util.Log
import com.example.innocare.model.Food
import com.example.innocare.model.UsersDetails
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class FoodDao {

    val db = FirebaseFirestore.getInstance()
    val foodCollections = db.collection("foods")
    val auth = Firebase.auth

    fun takeFood(foodName: String,
                 fats: Int,
                 protein: Int,
                 carbohydrates: Int,
                 calories: Int,
                 qty: Int,
                 type: String){

        val currentUserId = auth.currentUser!!.uid
        CoroutineScope(Dispatchers.IO).launch {
            try{

                val userDetailsDao = UsersDetailsDao()
                val user = userDetailsDao.getUserById(currentUserId).await().toObject(UsersDetails::class.java)

                val currentTime = System.currentTimeMillis()
                val food = user?.let {
                    Food(foodName, fats, protein, carbohydrates, calories, qty, it.uid, currentTime,
                            type)
                }
                foodCollections.document().set(food!!)
            } catch (e: Exception) {
                Log.d("TAG", "takeFood: ${e.message}")
            }
        }
    }

    fun getFoodById(uId: String): Task<DocumentSnapshot> {
        return foodCollections.document(uId).get()
    }
}