package com.dicoding.picodiploma.loginwithanimation.view.stories

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.api.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityDetailStoryBinding

class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = getString(R.string.detail_story)

        val story = intent.getParcelableExtra<ListStoryItem>(STORY) as ListStoryItem

        Glide
            .with(this)
            .load(story.photoUrl)
            .placeholder(R.drawable.baseline_image_24)
            .into(binding.ivPicture)
        binding.tvTitle.text = story.name
        binding.tvDescription.text = story.description
    }


    companion object {
        const val STORY = "story"
    }
}

