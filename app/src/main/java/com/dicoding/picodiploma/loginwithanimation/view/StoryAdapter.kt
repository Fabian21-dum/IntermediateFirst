package com.dicoding.picodiploma.loginwithanimation.view

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.api.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.databinding.StoryitemsBinding
import com.dicoding.picodiploma.loginwithanimation.view.stories.DetailStoryActivity
import com.dicoding.picodiploma.loginwithanimation.view.stories.DetailStoryActivity.Companion.STORY

class StoryAdapter : ListAdapter<ListStoryItem, StoryAdapter.StoryViewHolder>(DIFF_CALLBACK) {

    private var onItemClickCallback: OnItemClickCallback? = null

    inner class StoryViewHolder(private val binding: StoryitemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(story: ListStoryItem) {
            binding.tvItemName.text = story.name
            binding.tvItemDescription.text = story.description
            Glide.with(binding.root.context)
                .load(story.photoUrl)
                .into(binding.imgItemPhoto)
            binding.cardView.setOnClickListener {
                val intent = Intent(itemView.context, DetailStoryActivity::class.java)
                intent.putExtra(STORY, story)

                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): StoryViewHolder {
        val binding =
            StoryitemsBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: StoryViewHolder, position: Int) {
        val story = getItem(position) // Use getItem() from ListAdapter
        viewHolder.bind(story)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(storyId: String)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                // Compare unique IDs
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                // Compare entire content
                return oldItem == newItem
            }
        }
    }
}