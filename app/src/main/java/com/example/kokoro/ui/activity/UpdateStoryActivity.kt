package com.example.kokoro.ui.activity

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.kokoro.R
import com.example.kokoro.databinding.ActivityUpdateStoryBinding
import com.example.kokoro.repository.StoryRepositoryImpl
import com.example.kokoro.utils.ImageUtils
import com.example.kokoro.viewModel.StoryViewModel
import com.squareup.picasso.Picasso

class UpdateStoryActivity : AppCompatActivity() {

   lateinit var binding: ActivityUpdateStoryBinding
   lateinit var storyViewModel: StoryViewModel
     lateinit var imageUtils: ImageUtils

     var storyId: String? = null
     var imageUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUpdateStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repo = StoryRepositoryImpl()
        storyViewModel = StoryViewModel(repo)
        imageUtils = ImageUtils(this)

        storyId = intent.getStringExtra("storyId")

        storyId?.let {
            storyViewModel.getStoryById(it)
        }

        storyViewModel.stories.observe(this) { story ->
            if (story != null) {
                binding.editStoryTitle.setText(story.storyTitle ?: "")
                binding.editStoryInput.setText(story.storyDesc ?: "")

                imageUrl = story.imageUrl

                if (!imageUrl.isNullOrEmpty()) {
                    Picasso.get().load(imageUrl).into(binding.editImageBrowse)
                }
            }
        }

        // Handle new image selection
        imageUtils.registerActivity { uri ->
            uri?.let {
                imageUrl = it.toString()
                binding.editImageBrowse.setImageURI(uri)
            }
        }

        binding.editImageBrowse.setOnClickListener {
            imageUtils.launchGallery(this)
        }

        // Update story button
        binding.editStoryButton.setOnClickListener {
            updateStory()
        }

        // Handle window insets for proper UI rendering
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun updateStory() {
        val storyTitle = binding.editStoryTitle.text.toString().trim()
        val storyDesc = binding.editStoryInput.text.toString().trim()

        if (storyTitle.isEmpty() || storyDesc.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val updatedMap = mutableMapOf<String, Any>(
            "storyTitle" to storyTitle,
            "storyDesc" to storyDesc
        )

        imageUrl?.let {
            updatedMap["imageUrl"] = it
        }

        storyId?.let { id ->
            storyViewModel.updateStory(id, updatedMap) { success, message ->
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                if (success) finish()
            }
        }
    }
}
