package com.example.kokoro.ui.activity

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.kokoro.R
import com.example.kokoro.databinding.ActivityAddBookBinding
import com.example.kokoro.model.BookModel
import com.example.kokoro.model.StoryModel
import com.example.kokoro.repository.BookRepositoryImpl
import com.example.kokoro.repository.StoryRepositoryImpl
import com.example.kokoro.utils.ImageUtils
import com.example.kokoro.utils.LoadingUtils
import com.example.kokoro.viewModel.BookViewModel
import com.example.kokoro.viewModel.StoryViewModel
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Locale

class AddBookActivity : AppCompatActivity() {

    lateinit var binding: ActivityAddBookBinding
    lateinit var bookViewModel: BookViewModel
    lateinit var loadingUtils: LoadingUtils
    lateinit var imageUtils: ImageUtils

    var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imageUtils = ImageUtils(this)
        loadingUtils = LoadingUtils(this)

        val repo = BookRepositoryImpl()
        bookViewModel = BookViewModel(repo)

        imageUtils.registerActivity { url ->
            url?.let {
                imageUri = it
                Picasso.get().load(it).into(binding.bookImageBrowse)
            }
        }

        binding.bookImageBrowse.setOnClickListener {
            imageUtils.launchGallery(this)
        }

        binding.submitStoryButton.setOnClickListener {
            uploadImage()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    private fun uploadImage() {
        loadingUtils.show()

        imageUri?.let { uri ->
            bookViewModel.uploadBookImage(this, uri) { imageUrl ->
                Log.d("Upload", imageUrl.toString())
                if (imageUrl != null) {
                    addBook(imageUrl)
                } else {
                    Log.e("Upload Error", "Failed to upload image")
                }
            }
        }
    }
    private fun addBook(url: String) {
        val bookTitle = binding.bookTitle.text.toString()
        val bookGenre = binding.bookGenre.text.toString()
        val bookAuthor = binding.bookAuthor.text.toString()
        val bookDesc = binding.bookInput.text.toString()


        val bookModel = BookModel(
            "", bookTitle,bookGenre,bookAuthor, bookDesc, url
        )

        bookViewModel.addBook(bookModel) { success, message ->
            loadingUtils.dismiss()
            if (success) {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                finish()
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            }
        }
    }
}