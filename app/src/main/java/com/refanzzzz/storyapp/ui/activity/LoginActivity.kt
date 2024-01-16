package com.refanzzzz.storyapp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.refanzzzz.storyapp.data.Result
import com.refanzzzz.storyapp.databinding.ActivityLoginBinding
import com.refanzzzz.storyapp.ui.viewmodel.UserViewModel
import com.refanzzzz.storyapp.ui.viewmodel.ViewModelFactory

class LoginActivity : AppCompatActivity() {

    private var _activityLoginBinding: ActivityLoginBinding? = null
    private val binding get() = _activityLoginBinding

    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()

        userViewModel = obtainViewModel(this@LoginActivity)


        binding?.btnLogin?.setOnClickListener {
            login()
        }
    }

    private fun initBinding() {
        _activityLoginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding?.root)
    }

    private fun obtainViewModel(activity: AppCompatActivity): UserViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[UserViewModel::class.java]
    }

    private fun showLoading(isLoading: Boolean) {
        binding?.progressBarLogin?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun login() {
        binding?.let {
            val email = it.editTextEmail.text.toString()
            val password = it.editTextPassword.text.toString()

            userViewModel.login(email, password)
                .observe(this@LoginActivity) { result ->
                    if (result != null) {
                        when (result) {
                            is Result.Loading -> {
                                showLoading(true)
                            }

                            is Result.Success -> {
                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                startActivity(intent)
                                finishAffinity()

                                showLoading(false)
                            }

                            is Result.Error -> {
                                showToast(result.error)
                                showLoading(false)
                            }
                        }
                    }

                }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _activityLoginBinding = null
    }
}