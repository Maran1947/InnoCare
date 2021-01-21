package com.example.innocare

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_track_calorie.*

class TrackCalorieActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
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