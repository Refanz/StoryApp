package com.refanzzzz.storyapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.refanzzzz.storyapp.data.repository.StoryRepository

class MapsViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    fun getStoriesWithLocation() = storyRepository.getStoriesWithLocation()
}