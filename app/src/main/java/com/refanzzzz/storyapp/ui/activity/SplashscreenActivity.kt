package com.refanzzzz.storyapp.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.refanzzzz.storyapp.R
import com.refanzzzz.storyapp.ui.viewmodel.UserViewModel
import com.refanzzzz.storyapp.ui.viewmodel.ViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SplashscreenActivity : AppCompatActivity() {

    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)

        userViewModel = obtainViewModel(this@SplashscreenActivity)

        lifecycleScope.launch(Dispatchers.Default) {

            delay(2_000)

            withContext(Dispatchers.Main) {

                userViewModel.getInfoLogin().observe(this@SplashscreenActivity) {
                    if (!it.token.isNullOrBlank()) {
                       moveActivity(MainActivity())
                    } else {
                        moveActivity(WelcomeActivity())
                    }
                }


            }
        }
    }

    private fun moveActivity(activity: AppCompatActivity) {
        val intent = Intent(this@SplashscreenActivity, activity::class.java)
        startActivity(intent)
        finish()
    }

    private fun obtainViewModel(activity: AppCompatActivity) : UserViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[UserViewModel::class.java]
    }
}