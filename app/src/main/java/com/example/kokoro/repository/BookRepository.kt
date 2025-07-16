package com.example.kokoro.repository

import android.content.Context
import android.net.Uri
import com.example.kokoro.model.BookModel

interface BookRepository {
    fun addBook(bookModel: BookModel,
                callback: (Boolean, String) -> Unit)

    fun updateBook(bookId: String,
                    data: MutableMap<String, Any>,
                    callback: (Boolean, String) -> Unit)

    fun deleteBook(bookId: String,
                    callback: (Boolean, String) -> Unit)

    fun getBookById(bookId: String,
                     callback: (BookModel?, Boolean, String) -> Unit)

    fun getAllBook(callback: (List<BookModel>?, Boolean, String) -> Unit)

    fun uploadBookImage(context: Context, imageUri: Uri, callback: (String?) -> Unit)

    fun getBookFileNameFromUri(context: Context, uri: Uri): String?
}
