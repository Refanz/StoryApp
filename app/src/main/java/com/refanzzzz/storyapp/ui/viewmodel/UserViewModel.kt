package com.refanzzzz.storyapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.refanzzzz.storyapp.data.repository.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun register(name: String, email: String, password: String) =
        userRepository.register(name, email, password)

    fun login(email: String, password: String) = userRepository.login(email, password)

    fun getInfoLogin() = userRepository.getInfoUserLogin()

    fun removeUserLogin() {
        viewModelScope.launch {
            userRepository.removeUserLogin()
        }
    }
}