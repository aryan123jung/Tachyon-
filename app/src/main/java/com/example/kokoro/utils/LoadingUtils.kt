package com.example.kokoro.utils


import android.app.Activity
import android.app.AlertDialog
import com.example.kokoro.R

class LoadingUtils(val activity: Activity) {
    lateinit var altertDialog: AlertDialog
    fun show(){
        val dialogView = activity.layoutInflater.inflate(
            R.layout.loading,
            null)

        val builder = AlertDialog.Builder(activity)
        builder.setView(dialogView)
        builder.setCancelable(false)

        altertDialog = builder.create()
        altertDialog.show()
    }

    fun dismiss(){
        altertDialog.dismiss()
    }

}