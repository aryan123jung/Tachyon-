package com.example.tachyon.repository

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.OpenableColumns
import com.cloudinary.Cloudinary
import com.cloudinary.utils.ObjectUtils
import com.example.tachyon.model.BookModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.io.InputStream
import java.util.concurrent.Executors

class BookRepositoryImpl:BookRepository {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val reference: DatabaseReference = database.reference.child("books")

    override fun addBook(bookModel: BookModel, callback: (Boolean, String) -> Unit) {
        val id = reference.push().key.toString()
        bookModel.bookId = id
        bookModel.timestamp = System.currentTimeMillis()

        reference.child(id).setValue(bookModel)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    callback(true, "Book Added Successfully")
                }else{
                    callback(false,"${it.exception?.message}")
                }
            }
    }

    override fun updateBook(
        bookId: String,
        data: MutableMap<String, Any>,
        callback: (Boolean, String) -> Unit
    ) {
        data["timestamp"] = System.currentTimeMillis()

        reference.child(bookId).updateChildren(data)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    callback(true,"Book Updated Successfully")
                }else{
                    callback(false,"${it.exception?.message}")
                }
            }
    }

    override fun deleteBook(bookId: String, callback: (Boolean, String) -> Unit) {
        reference.child(bookId).removeValue()
            .addOnCompleteListener {
                if(it.isSuccessful){
                    callback(true, "Book deleted successfully")
                }else{
                    callback(false, "${it.exception?.message}")
                }
            }
    }

    override fun getBookById(bookId: String, callback: (BookModel?, Boolean, String) -> Unit) {
        reference.child(bookId).addValueEventListener(
            object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        val model = snapshot.getValue(BookModel::class.java)
                        callback(model,true,"Book fetched")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(null,false,error.message)
                }
            }
        )
    }

    override fun getAllBook(callback: (List<BookModel>?, Boolean, String) -> Unit) {
        reference.addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val books = mutableListOf<BookModel>()
                    if (snapshot.exists()) {
                        for (eachBook in snapshot.children) {
                            val model = eachBook.getValue(BookModel::class.java)
                            if (model != null) {
                                books.add(model)
                            }
                        }
                        books.sortByDescending { it.timestamp }
                        callback(books, true, "Fetched")
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
                "cloud_name" to "ddlm4gpfk",
                "api_key" to "476249442996575",
                "api_secret" to "ZMsR2wnmhoTJT4E3mCrtBlef6Vo"
            )
    )

    override fun uploadBookImage(context: Context, imageUri: Uri, callback: (String?) -> Unit) {
        val executor = Executors.newSingleThreadExecutor()
        executor.execute {
            try {
                val inputStream: InputStream? = context.contentResolver.openInputStream(imageUri)
                var fileName = getBookFileNameFromUri(context, imageUri)

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


    override fun getBookFileNameFromUri(context: Context, uri: Uri): String? {
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