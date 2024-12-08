package com.dicoding.picodiploma.loginwithanimation.di

import android.content.Context
import com.dicoding.picodiploma.loginwithanimation.api.ApiConfig
import com.dicoding.picodiploma.loginwithanimation.data.StoryRepository
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserPreference
import com.dicoding.picodiploma.loginwithanimation.data.pref.dataStore

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = ApiConfig.getApiService(pref)
        return UserRepository.getInstance(pref, user)
    }
    fun provideStoryRepository(context: Context): StoryRepository{
        val pref = UserPreference.getInstance(context.dataStore)
        val user = ApiConfig.getApiService(pref)
        return StoryRepository.getInstance(user)
    }
}