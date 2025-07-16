package com.example.kokoro.viewModel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.example.kokoro.model.StoryModel
import com.example.kokoro.repository.StoryRepository

class StoryViewModel(val repo: StoryRepository) {

    fun addStory(storyModel: StoryModel, callback: (Boolean, String) -> Unit) {
        repo.addStory(storyModel, callback)
    }

    fun updateStory(storyId: String, data: MutableMap<String, Any>, callback: (Boolean, String) -> Unit) {
        repo.updateStory(storyId, data, callback)
    }

    fun deleteStory(storyId: String, callback: (Boolean, String) -> Unit) {
        repo.deleteStory(storyId, callback)
    }
    var _stories = MutableLiveData<StoryModel?>()
    var stories = MutableLiveData<StoryModel?>()
        get() = _stories

    var _allStories = MutableLiveData<List<StoryModel>?>()
    var allStories = MutableLiveData<List<StoryModel>?>()
        get() = _allStories

    var _loading = MutableLiveData<Boolean>()
    var loading = MutableLiveData<Boolean>()
        get() = _loading


    fun getStoryById(storyId: String){
        repo.getStoryById(storyId){
            story,success,message->
            if(success){
                _stories.value = story
            }
        }
    }

    fun getAllStory() {
        repo.getAllStory { stories, success, message ->
            if (success) {
                _allStories.value = stories
            }
        }
    }

    fun uploadImage(context: Context, imageUri: Uri, callback: (String?) -> Unit) {
        repo.uploadImage(context, imageUri, callback)
    }
}
