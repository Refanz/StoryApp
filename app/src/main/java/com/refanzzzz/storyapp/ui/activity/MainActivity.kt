package com.refanzzzz.storyapp.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.refanzzzz.storyapp.R
import com.refanzzzz.storyapp.databinding.ActivityMainBinding
import com.refanzzzz.storyapp.ui.fragment.StoryFragment
import com.refanzzzz.storyapp.ui.viewmodel.UserViewModel
import com.refanzzzz.storyapp.ui.viewmodel.ViewModelFactory

class MainActivity : AppCompatActivity() {

    private var _activityMainBinding: ActivityMainBinding? = null
    private val binding get() = _activityMainBinding

    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()

        userViewModel = obtainViewModel(this@MainActivity)

        replaceFragment(StoryFragment())

        binding?.topAppBar?.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu1 -> {
                    showLogoutDialog()
                    true
                }

                R.id.menu2 -> {
                    val intent = Intent(this@MainActivity, MapsActivity::class.java)
                    startActivity(intent)
                    true
                }

                else -> false
            }
        }

        binding?.let {
            it.btnAddStory.setOnClickListener {
                val intent = Intent(this@MainActivity, AddStoryActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun initBinding() {
        _activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager

        fragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    private fun showLogoutDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this@MainActivity)
        builder.apply {
            setMessage("Logout?")
            setPositiveButton("Ya") { _, _ ->
                userViewModel.removeUserLogin()

                val intent = Intent(this@MainActivity, WelcomeActivity::class.java)
                startActivity(intent)

                finish()
            }
            setNegativeButton("Tidak") { dialog, _ ->
                dialog.dismiss()
            }
            show()
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): UserViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[UserViewModel::class.java]
    }


    override fun onDestroy() {
        super.onDestroy()

        _activityMainBinding = null
    }
}