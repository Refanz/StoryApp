package com.refanzzzz.storyapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.refanzzzz.storyapp.data.repository.StoryRepository
import java.io.File

class StoryViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    fun addNewStory(file: File, description: String) =
        storyRepository.addNewStory(file, description)

    fun getAllStory() = storyRepository.getAllStory()
}