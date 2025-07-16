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
import com.example.kokoro.databinding.ActivityAddStoryBinding
import com.example.kokoro.model.StoryModel
import com.example.kokoro.repository.StoryRepositoryImpl
import com.example.kokoro.utils.ImageUtils
import com.example.kokoro.utils.LoadingUtils
import com.example.kokoro.viewModel.StoryViewModel
import com.squareup.picasso.Picasso

class AddStoryActivity : AppCompatActivity() {

    lateinit var binding: ActivityAddStoryBinding
    lateinit var storyViewModel: StoryViewModel
    lateinit var loadingUtils: LoadingUtils
    lateinit var imageUtils: ImageUtils

    var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imageUtils = ImageUtils(this)
        loadingUtils = LoadingUtils(this)

        val repo = StoryRepositoryImpl()
        storyViewModel = StoryViewModel(repo)

        imageUtils.registerActivity { url ->
            url?.let {
                imageUri = it
                Picasso.get().load(it).into(binding.imageBrowse)
            }
        }

        binding.imageBrowse.setOnClickListener {
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
            storyViewModel.uploadImage(this, uri) { imageUrl ->
                Log.d("Upload", imageUrl.toString())
                if (imageUrl != null) {
                    addStory(imageUrl)
                } else {
                    Log.e("Upload Error", "Failed to upload image")
                }
            }
        }
    }

    private fun addStory(url: String) {
        val storyTitle = binding.storyTitle.text.toString()
        val storyDesc = binding.storyInput.text.toString()

        val storyModel = StoryModel(
            "", storyTitle, storyDesc, url
        )

        storyViewModel.addStory(storyModel) { success, message ->
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
