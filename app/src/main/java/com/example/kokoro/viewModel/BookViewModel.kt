package com.example.kokoro.viewModel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.example.kokoro.model.BookModel
import com.example.kokoro.model.StoryModel
import com.example.kokoro.repository.BookRepository

class BookViewModel(val repo: BookRepository) {

    fun addBook(bookModel: BookModel,
                callback: (Boolean, String) -> Unit){
        repo.addBook(bookModel,callback)
    }

    fun updateBook(bookId: String,
                   data: MutableMap<String, Any>,
                   callback: (Boolean, String) -> Unit){
        repo.updateBook(bookId,data,callback)
    }

    fun deleteBook(bookId: String,
                   callback: (Boolean, String) -> Unit){
        repo.deleteBook(bookId,callback)
    }

    var _books = MutableLiveData<BookModel?>()
    var books = MutableLiveData<BookModel?>()
        get() = _books

    var _allBooks = MutableLiveData<List<BookModel>?>()
    var allBooks = MutableLiveData<List<BookModel>?>()
        get() = _allBooks

    var _loading = MutableLiveData<Boolean>()
    var loading = MutableLiveData<Boolean>()
        get() = _loading


    fun getBookById(bookId: String){
        repo.getBookById(bookId){
            book,success,message->
            if(success){
                _books.value = book
            }
        }

    }

    fun getAllBook(){
        repo.getAllBook{ books, success, message ->
            if(success){
                _allBooks.value = books
            }
        }
    }

    fun uploadBookImage(context: Context, imageUri: Uri, callback: (String?) -> Unit){
        repo.uploadBookImage(context,imageUri,callback)
    }
}