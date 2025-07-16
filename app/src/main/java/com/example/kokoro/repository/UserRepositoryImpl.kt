package com.example.kokoro.repository

import com.example.kokoro.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class UserRepositoryImpl: UserRepository {
    var databse : FirebaseDatabase = FirebaseDatabase.getInstance()
    var reference = databse.reference.child("users")

    var auth : FirebaseAuth = FirebaseAuth.getInstance()


    override fun login(email: String, password: String, callback: (Boolean, String) -> Unit) {
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
            if(it.isSuccessful){
                callback(true,"Logging In")
            }else{
                callback(false, it.exception?.message.toString())
            }
        }
    }

    override fun register(
        email: String,
        password: String,
        callback: (Boolean, String, String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
            if(it.isSuccessful){
                callback(true,"Registration successfull",
                    auth.currentUser?.uid.toString())
            }else{
                callback(false, it.exception?.message.toString(),"")
            }
        }

    }


    override fun forgotPassword(email: String, callback: (Boolean, String) -> Unit) {
        auth.sendPasswordResetEmail(email).addOnCompleteListener {
            if(it.isSuccessful){
                callback(true,"Password Changed Successfully")
            }else{
                callback(false, it.exception?.message.toString())
            }
        }
    }

    override fun addUserToDatabase(
        userId: String,
        userModel: UserModel,
        callback: (Boolean, String) -> Unit
    ) {
        reference.child(userId.toString()).setValue(userModel).addOnCompleteListener {
            if(it.isSuccessful){
                callback(true,"User Added Successfully")
            }else{
                callback(false, it.exception?.message.toString())
            }
        }
    }

    override fun logout(callback: (Boolean, String) -> Unit) {
        try {
            auth.signOut()
            callback(true,"Logout Successfull")
        }catch (e:Exception){
            callback(false, e.message.toString())
        }
    }

    override fun editProfile(
        userId: String,
        data: MutableMap<String, Any>,
        callback: (Boolean, String) -> Unit
    ) {
        reference.child(userId).updateChildren(data).addOnCompleteListener {
            if(it.isSuccessful){
                callback(true,"Profile Updated")
            }else{
                callback(false, it.exception?.message.toString())
            }
        }
    }

    override fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    override fun getUserFromDatabase(userId: String, callback: (UserModel?, Boolean, String) -> Unit) {
        reference.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val userModel = snapshot.getValue(UserModel::class.java)
                    callback(userModel, true, "User Fetched Successfully")
                } else {
                    callback(null, false, "User Not Found")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null, false, error.message)
            }
        })
    }

}