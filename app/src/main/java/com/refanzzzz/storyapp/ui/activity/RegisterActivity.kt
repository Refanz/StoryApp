package com.refanzzzz.storyapp.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.refanzzzz.storyapp.R
import com.refanzzzz.storyapp.data.Result
import com.refanzzzz.storyapp.databinding.ActivityRegisterBinding
import com.refanzzzz.storyapp.ui.viewmodel.UserViewModel
import com.refanzzzz.storyapp.ui.viewmodel.ViewModelFactory

class RegisterActivity : AppCompatActivity() {

    private var _activityRegisterBinding: ActivityRegisterBinding? = null
    private val binding get() = _activityRegisterBinding

    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()

        userViewModel = obtainViewModel(this@RegisterActivity)

        binding?.btnRegister?.setOnClickListener {
            register()
        }
    }

    private fun initBinding() {
        _activityRegisterBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding?.root)
    }

    private fun showLoading(isLoading: Boolean) {
        binding?.progressBarRegister?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun register() {

        binding?.let {
            val name = it.editTextNama.text.toString()
            val email = it.editTextEmail.text.toString()
            val password = it.editTextPassword.text.toString()

            userViewModel.register(name, email, password).observe(this@RegisterActivity) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            showLoading(true)
                        }

                        is Result.Success -> {
                            showToast(resources.getString(R.string.register_success))
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
        Toast.makeText(this@RegisterActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun obtainViewModel(activity: AppCompatActivity): UserViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[UserViewModel::class.java]
    }

    override fun onDestroy() {
        super.onDestroy()

        _activityRegisterBinding = null
    }
}