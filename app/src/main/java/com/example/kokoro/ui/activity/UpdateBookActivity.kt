package com.example.kokoro.ui.activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.kokoro.R
import com.example.kokoro.databinding.ActivityUpdateBookBinding
import com.example.kokoro.repository.BookRepositoryImpl
import com.example.kokoro.utils.ImageUtils
import com.example.kokoro.viewModel.BookViewModel
import com.squareup.picasso.Picasso

class UpdateBookActivity : AppCompatActivity() {

    lateinit var binding:ActivityUpdateBookBinding
    lateinit var bookViewModel:BookViewModel
    lateinit var imageUtils: ImageUtils

    var bookId: String? = null
    var imageUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding= ActivityUpdateBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repo = BookRepositoryImpl()
        bookViewModel = BookViewModel(repo)
        imageUtils = ImageUtils(this)
        bookId = intent.getStringExtra("bookId")

        bookId?.let {
            bookViewModel.getBookById(it)
        }

        bookViewModel.books.observe(this) { book ->
            if (book != null) {
                binding.bookTitleUpdate.setText(book.bookTitle ?: "")
                binding.bookGenreUpdate.setText(book.bookGenre ?: "")
                binding.bookAuthorUpdate.setText(book.bookAuthor ?: "")
                binding.bookInputUpdate.setText(book.bookThoughts ?: "")
                imageUrl = book.bookimageUrl
                if (!imageUrl.isNullOrEmpty()) {
                    Picasso.get().load(imageUrl).into(binding.bookImageBrowseUpdate)
                }
            }
        }

        // Handle new image selection
        imageUtils.registerActivity { uri ->
            uri?.let {
                imageUrl = it.toString()
                binding.bookImageBrowseUpdate.setImageURI(uri)
            }
        }
        binding.bookImageBrowseUpdate.setOnClickListener {
            imageUtils.launchGallery(this)
        }

        // Update story button
        binding.updateBookButton.setOnClickListener {
            updateBook()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    private fun updateBook() {
        val bookTitle = binding.bookTitleUpdate.text.toString().trim()
        val bookGenre = binding.bookGenreUpdate.text.toString().trim()
        val bookAuthor = binding.bookAuthorUpdate.text.toString().trim()
        val bookDesc = binding.bookInputUpdate.text.toString().trim()

        if (bookTitle.isEmpty() || bookGenre.isEmpty()|| bookAuthor.isEmpty() || bookDesc.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val updatedMap = mutableMapOf<String, Any>(
            "bookTitle" to bookTitle,
            "bookGenre" to bookGenre,
            "bookAuthor" to bookAuthor,
            "bookThoughts" to bookDesc,
        )

        imageUrl?.let {
            updatedMap["bookimageUrl"] = it
        }
        bookId?.let { id ->
            bookViewModel.updateBook(id, updatedMap) { success, message ->
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                if (success) finish()
            }
        }
    }
    }