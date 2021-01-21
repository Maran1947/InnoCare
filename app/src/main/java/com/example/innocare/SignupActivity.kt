package com.example.innocare

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.innocare.dao.UsersDetailsDao
import com.example.innocare.model.UsersDetails
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class SignupActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    var activity = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        auth = FirebaseAuth.getInstance()

        btn_signup.setOnClickListener {
            registerUser()
        }

        btn_signin.setOnClickListener {
            val intent = Intent(this, SigninActivity::class.java)
            startActivity(intent)
        }

        val customActivities = listOf("Sedentary (little or no exercise)",
            "Lightly active (light exercise/sports 1–3 days/week)",
            "Moderately active (moderate exercise/sports 3–5 days/week)",
            "Extra active (very hard exercise/sports &amp; a physical job)"
        )

        val adapter = ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, customActivities)
        sp_option.adapter = adapter

        sp_option.onItemSelectedListener  = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                activity = adapterView?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }

    override fun onStart() {
        super.onStart()
        checkLoggedInState()
    }

    private fun registerUser() {
        val email = et_email.text.toString()
        val password = et_password.text.toString()
        val name = et_name.text.toString()
        val weight = et_weight.text.toString().toInt()
        val height = et_height.text.toString().toInt()
        val age = et_age.text.toString().toInt()

        val checkGender = rdb_gender.checkedRadioButtonId
        val gender = findViewById<RadioButton>(checkGender).text.toString()

        if(email.isNotEmpty() && password.isNotEmpty()) {

            btn_signup.visibility = View.GONE
            progressbar.visibility = View.VISIBLE

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    auth.createUserWithEmailAndPassword(email, password).await()
                    withContext(Dispatchers.Main) {

                        val usersDetails = auth.uid?.let { UsersDetails(it, name, weight, height, age, gender, activity) }
                        val userDetailsDao = UsersDetailsDao()
                        userDetailsDao.addUser(usersDetails)

                        checkLoggedInState()
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun checkLoggedInState() {
        if(auth.currentUser == null) {
//            Toast.makeText(applicationContext, "You are not logged in", Toast.LENGTH_LONG).show()
            progressbar.visibility = View.GONE
            btn_signup.visibility = View.VISIBLE
        } else {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}

