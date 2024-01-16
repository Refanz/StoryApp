package com.refanzzzz.storyapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.refanzzzz.storyapp.data.remote.response.ListStoryItem
import com.refanzzzz.storyapp.databinding.ItemStoryListBinding

class ListStoryAdapter(private val storyList: ArrayList<ListStoryItem>) :
    RecyclerView.Adapter<ListStoryAdapter.StoryViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding =
            ItemStoryListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return storyList.size
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = storyList[position]

        holder.binding.tvStoryDesc.text = story.description
        holder.binding.tvStoryTitle.text = story.name
        Glide.with(holder.binding.root).load(story.photoUrl).into(holder.binding.ivStory)

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(story, holder.itemView)
        }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class StoryViewHolder(var binding: ItemStoryListBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface OnItemClickCallback {
        fun onItemClicked(data: ListStoryItem, itemView: View)
    }
}