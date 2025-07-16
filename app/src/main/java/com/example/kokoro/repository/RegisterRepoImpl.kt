package com.example.kokoro.repository

import com.google.firebase.auth.FirebaseAuth

class RegisterRepoImpl( var auth : FirebaseAuth):RegisterRepo {
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
}