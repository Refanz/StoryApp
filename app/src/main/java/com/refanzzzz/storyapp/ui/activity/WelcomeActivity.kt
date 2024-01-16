package com.refanzzzz.storyapp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.refanzzzz.storyapp.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity(), View.OnClickListener {

    private var _activityWelcomeBinding: ActivityWelcomeBinding? = null
    private val binding get() = _activityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()

        binding?.apply {
            btnLogin.setOnClickListener(this@WelcomeActivity)
            btnRegister.setOnClickListener(this@WelcomeActivity)
        }
    }

    private fun initBinding() {
        _activityWelcomeBinding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding?.root)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding?.btnLogin?.id -> {
                val intent = Intent(this@WelcomeActivity, LoginActivity::class.java)
                startActivity(intent)
            }

            binding?.btnRegister?.id -> {
                val intent = Intent(this@WelcomeActivity, RegisterActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        _activityWelcomeBinding = null
    }
}