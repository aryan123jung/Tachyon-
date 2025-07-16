package com.example.kokoro.repository

import android.content.Context
import android.net.Uri
import androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback
import com.example.kokoro.model.StoryModel

interface StoryRepository {

    fun addStory(storyModel: StoryModel,
                 callback: (Boolean, String) -> Unit)

    fun updateStory(storyId: String,
                    data: MutableMap<String, Any>,
                    callback: (Boolean, String) -> Unit)

    fun deleteStory(storyId: String,
                    callback: (Boolean, String) -> Unit)

    fun getStoryById(storyId: String,
                     callback: (StoryModel?,Boolean, String) -> Unit)

    fun getAllStory(callback: (List<StoryModel>?, Boolean, String) -> Unit)

    fun uploadImage(context: Context, imageUri: Uri, callback: (String?) -> Unit)

    fun getFileNameFromUri(context: Context, uri: Uri): String?
}