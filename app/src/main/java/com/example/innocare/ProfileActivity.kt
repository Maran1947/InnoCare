package com.example.innocare

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.innocare.dao.UsersDetailsDao
import com.example.innocare.model.UsersDetails
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ProfileActivity : AppCompatActivity() {

    val auth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        setSupportActionBar(toolbar_profile)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val currentUserId = auth.currentUser!!.uid
        CoroutineScope(Dispatchers.IO).launch {
            try{
                val userDetailsDao = UsersDetailsDao()
                val user = userDetailsDao.getUserById(currentUserId).await().toObject(UsersDetails::class.java)

                withContext(Dispatchers.Main){

                    tv_name.text = user!!.name.toString()
                    tv_age.text = user.age.toString()
                    tv_gender.text = user.gender.toString()
                    tv_weight.text = user.weight.toString()
                    tv_height.text = user.height.toString()
                    tv_activity.text = user.activity.toString()
                }
            } catch (e: Exception) {
//                Log.d("TAG", "takeFood: ${e.message}")
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true

    }
}