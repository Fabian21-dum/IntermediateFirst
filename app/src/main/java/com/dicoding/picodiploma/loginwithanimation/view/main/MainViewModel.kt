package com.dicoding.picodiploma.loginwithanimation.view.main

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.StoryRepository
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import kotlinx.coroutines.launch
import java.io.File

class MainViewModel(private val repository: UserRepository, private val storyRepository: StoryRepository) : ViewModel() {
    private var _currentImageUri = MutableLiveData<Uri?>()
    val currentImageUri: MutableLiveData<Uri?> = _currentImageUri
    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun getAllStories() = storyRepository.getStory()

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
    fun addStory(imageFile: File, desc: String) = storyRepository.uploadStory(imageFile, desc)

    fun setCurrentImageUri(uri: Uri?) {
        _currentImageUri.value = uri
    }

}