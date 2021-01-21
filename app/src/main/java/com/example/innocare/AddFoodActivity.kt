package com.example.innocare

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.innocare.adapters.FoodAdapter
import com.example.innocare.dao.FoodDao
import com.example.innocare.model.Food
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_add_food.*

class AddFoodActivity : AppCompatActivity() {

    private lateinit var foodDao: FoodDao
    private lateinit var adapter: FoodAdapter

    val auth = Firebase.auth
    private var type:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_food)

        type = intent.getStringExtra("EXTRA_TYPE")
        tv_type.text = type
//        Log.d("TAG", "onCreate: $type")

        fab.setOnClickListener{
            Intent(this, TakeAFoodActivity::class.java).also {
                it.putExtra("EXTRA_TYPE", type)
                startActivity(it)
            }
        }

        foodDao = FoodDao()
        setUpRecyclerView()

    }

    private fun setUpRecyclerView() {
        val foodsCollections = foodDao.foodCollections
        val query = foodsCollections
                .whereEqualTo("createdBy", auth.currentUser!!.uid)
                .whereEqualTo("type", type)
                .orderBy("createdAt", Query.Direction.DESCENDING)

        val recyclerOptions = FirestoreRecyclerOptions.Builder<Food>().setQuery(query, Food::class.java).build()

        adapter = FoodAdapter(recyclerOptions)

        rv_food.adapter = adapter
        rv_food.layoutManager = LinearLayoutManager(this)
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

}