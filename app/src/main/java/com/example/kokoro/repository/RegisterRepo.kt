package com.example.kokoro.repository

interface RegisterRepo {

    fun register(email: String,password: String,
                 callback: (Boolean, String, String) -> Unit)

}