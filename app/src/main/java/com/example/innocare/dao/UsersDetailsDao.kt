package com.example.innocare.dao

import com.example.innocare.model.UsersDetails
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UsersDetailsDao {
    val db = FirebaseFirestore.getInstance()
    val usersCollection = db.collection("users")

    fun addUser(usersDetails: UsersDetails?) {
        usersDetails?.let {
            CoroutineScope(Dispatchers.IO).launch {
                usersCollection.document(usersDetails.uid).set(it)
            }
        }
    }

    fun getUserById(uId: String): Task<DocumentSnapshot> {
        return usersCollection.document(uId).get()
    }
}