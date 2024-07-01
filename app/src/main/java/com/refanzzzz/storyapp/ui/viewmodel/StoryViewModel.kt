package com.refanzzzz.storyapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.refanzzzz.storyapp.data.remote.response.ListStoryItem
import com.refanzzzz.storyapp.data.repository.StoryRepository
import java.io.File

class StoryViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    fun addNewStory(file: File, description: String) =
        storyRepository.addNewStory(file, description)

    fun getAllStory() = storyRepository.getAllStory()

    val story: LiveData<PagingData<ListStoryItem>> =
        storyRepository.getStories().cachedIn(viewModelScope)
}