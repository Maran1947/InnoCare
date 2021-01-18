package com.example.innocare

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.innocare.dao.UsersDetailsDao
import com.example.innocare.model.Food
import com.example.innocare.model.UsersDetails
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_track_calorie.*
import kotlinx.android.synthetic.main.recycler_card_layout.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class TrackCalorieActivity : AppCompatActivity() {

    val db = FirebaseFirestore.getInstance()
    val foodCollections = db.collection("foods")
    val userCollections = db.collection("users")
    val auth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_track_calorie)


        cd_breakfast.setOnClickListener {
//            Toast.makeText(this, "Breakfast", Toast.LENGTH_SHORT).show()
            Intent(this, AddFoodActivity::class.java).also {
                it.putExtra("EXTRA_TYPE", "Breakfast")
                startActivity(it)
            }
        }

        cd_lunch.setOnClickListener {
//            Toast.makeText(this, "Breakfast", Toast.LENGTH_SHORT).show()
            Intent(this, AddFoodActivity::class.java).also {
                it.putExtra("EXTRA_TYPE", "Lunch")
                startActivity(it)
            }
        }

        cd_dinner.setOnClickListener {
//            Toast.makeText(this, "Breakfast", Toast.LENGTH_SHORT).show()
            Intent(this, AddFoodActivity::class.java).also {
                it.putExtra("EXTRA_TYPE", "Dinner")
                startActivity(it)
            }
        }


    }



}