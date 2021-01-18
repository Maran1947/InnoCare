package com.example.innocare

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast

import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.innocare.dao.UsersDetailsDao
import com.example.innocare.model.Food
import com.example.innocare.model.UsersDetails

import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {

    final var map = HashMap<String, Any>()

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbar: Toolbar
    private lateinit var navigationView: NavigationView


    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbar)
        navigationView = findViewById(R.id.navigation_view)


        auth = FirebaseAuth.getInstance()

        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)

        val drawerToggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navigationView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.nav_home -> {
//                    Toast.makeText(this,"Dashboard",Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }

                R.id.nav_track_calorie -> {
//                    Toast.makeText(this, "Add Food", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, TrackCalorieActivity::class.java)
                    startActivity(intent)
                }

                R.id.log_out -> {
//                    Toast.makeText(this,"Logout",Toast.LENGTH_SHORT).show()
                    auth.signOut()
                    val intent = Intent(this, SigninActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }

            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        val currentUserId = auth.currentUser!!.uid
        CoroutineScope(Dispatchers.IO).launch {
            try{

                val userDetailsDao = UsersDetailsDao()
                val user = userDetailsDao.getUserById(currentUserId).await().toObject(UsersDetails::class.java)

                withContext(Dispatchers.Main){
                    var bmr: Double = 0.00;
                    if(user!!.gender.equals("Male")) {
                        bmr = (10*user.weight+ 6.25*user.height + 5*user.age + 5)
                    } else if(user!!.gender.equals("Female")){
                        bmr = (10*user.weight + 6.25*user.height + 5*user.age - 161)
                    }

                    var cal:Int = 0

                    when(user!!.activity){
                        "Sedentary (little or no exercise)" -> {
                            cal = (bmr*1.2).toInt()
                        }
                        "Lightly active (light exercise/sports 1–3 days/week)" -> {
                            cal = (bmr*1.55).toInt()

                        }
                        "Moderately active (moderate exercise/sports 3–5 days/week)" -> {
                            cal = (bmr*1.725).toInt()

                        }
                        "Extra active (very hard exercise/sports &amp; a physical job)" -> {
                            cal = (bmr*1.9).toInt()

                        }
                    }

                    tv_week_calorie.text = cal.toString()
                }
            } catch (e: Exception) {
                Log.d("TAG", "takeFood: ${e.message}")
            }
        }



    }

    override fun onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
//            Log.d("TAG", "back")
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
//            Log.d("TAG", "back ")
            super.onBackPressed()
        }
    }
}
