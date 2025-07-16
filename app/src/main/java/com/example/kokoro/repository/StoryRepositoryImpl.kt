package com.example.kokoro.repository

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.OpenableColumns
import android.util.Log
import com.cloudinary.Cloudinary
import com.cloudinary.utils.ObjectUtils
import com.example.kokoro.model.StoryModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.io.InputStream
import java.util.concurrent.Executors

class StoryRepositoryImpl: StoryRepository {

    val database:FirebaseDatabase = FirebaseDatabase.getInstance()
    val reference: DatabaseReference = database.reference.child("stories")

    override fun addStory(storyModel: StoryModel, callback: (Boolean, String) -> Unit) {
        val id = reference.push().key.toString()
        storyModel.storyId = id
        storyModel.timestamp = System.currentTimeMillis() // Assign timestamp

        reference.child(id).setValue(storyModel)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    callback(true, "Story Added Successfully")
                } else {
                    callback(false, "${it.exception?.message}")
                }
            }
    }

    override fun updateStory(
        storyId: String,
        data: MutableMap<String, Any>,
        callback: (Boolean, String) -> Unit
    ) {
        data["timestamp"] = System.currentTimeMillis() // Update timestamp when story is edited

        reference.child(storyId).updateChildren(data)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    callback(true, "Story updated successfully")
                } else {
                    callback(false, "${it.exception?.message}")
                }
            }
    }


    override fun deleteStory(storyId: String, callback: (Boolean, String) -> Unit) {
        reference.child(storyId).removeValue()
            .addOnCompleteListener {
            if(it.isSuccessful){
                callback(true, "Story deleted successfully")
            }else{
                callback(false, "${it.exception?.message}")
            }
        }
    }

    override fun getStoryById(storyId: String, callback: (StoryModel?, Boolean, String) -> Unit) {
        reference.child(storyId).addValueEventListener(
            object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        var model = snapshot.getValue(StoryModel::class.java)
                        callback(model,true,"Story fetched")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(null,false,error.message)
                }
            }
        )
    }

    override fun getAllStory(callback: (List<StoryModel>?, Boolean, String) -> Unit) {
        reference.addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val stories = mutableListOf<StoryModel>()
                    if (snapshot.exists()) {
                        for (eachStory in snapshot.children) {
                            val model = eachStory.getValue(StoryModel::class.java)
                            if (model != null) {
                                stories.add(model)
                            }
                        }
                        stories.sortByDescending { it.timestamp } // Sorting by timestamp
                        callback(stories, true, "Fetched")
                    } else {
                        callback(null, false, "No data found")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(null, false, error.message)
                }
            }
        )
    }


    private val cloudinary = Cloudinary(
        mapOf(
            "cloud_name" to "dmnnqyjvj",
            "api_key" to "719664345337452",
            "api_secret" to "euvxoQyPG_ouM5nhLlZ3L6R5qIo"
        )
    )

    override fun uploadImage(context: Context, imageUri: Uri, callback: (String?) -> Unit) {
        val executor = Executors.newSingleThreadExecutor()
        executor.execute {
            try {
                val inputStream: InputStream? = context.contentResolver.openInputStream(imageUri)
                var fileName = getFileNameFromUri(context, imageUri)

                fileName = fileName?.substringBeforeLast(".") ?: "uploaded_image"

                val response = cloudinary.uploader().upload(
                    inputStream, ObjectUtils.asMap(
                        "public_id", fileName,
                        "resource_type", "image"
                    )
                )

                var imageUrl = response["url"] as String?

                imageUrl = imageUrl?.replace("http://", "https://")

                Handler(Looper.getMainLooper()).post {
                    callback(imageUrl)
                }

            } catch (e: Exception) {
                e.printStackTrace()
                Handler(Looper.getMainLooper()).post {
                    callback(null)
                }
            }
        }
    }

    override fun getFileNameFromUri(context: Context, uri: Uri): String? {
        var fileName: String? = null
        val cursor: Cursor? = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex != -1) {
                    fileName = it.getString(nameIndex)
                }
            }
        }
        return fileName
    }
}