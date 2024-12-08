package com.dicoding.picodiploma.loginwithanimation.view.login

import android.app.Application
import android.content.Context.MODE_PRIVATE
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.api.ApiConfig
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.Flow

class LoginViewModel (private val repository: UserRepository) : ViewModel() {
    fun saveSession(user: UserModel) {
        viewModelScope.launch { repository.saveSession(user) }
    }
    fun login(email: String, password: String) = repository.login(email, password)
}