package com.refanzzzz.storyapp.ui.activity

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.refanzzzz.storyapp.model.Story
import com.refanzzzz.storyapp.databinding.ActivityDetailStoryBinding
import com.refanzzzz.storyapp.ui.fragment.StoryFragment

class DetailStoryActivity : AppCompatActivity() {

    private var _activityDetailStoryBinding: ActivityDetailStoryBinding? = null
    private val binding get() = _activityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()

        binding?.let { v ->
            v.tvDetailName.text = getStoryFromIntent().name
            v.tvDetailDesc.text = getStoryFromIntent().desc
            v.tvDetailCreatedAt.text = getStoryFromIntent().createdAt

            Glide.with(this@DetailStoryActivity).load(getStoryFromIntent().imgUrl).into(v.ivDetailStory)
        }
    }

    private fun initBinding() {
        _activityDetailStoryBinding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding?.root)
    }

    private fun getStoryFromIntent(): Story {
        val story: Story? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(StoryFragment.STORY_KEY, Story::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(StoryFragment.STORY_KEY)
        }

        return story ?: Story()
    }

    override fun onDestroy() {
        super.onDestroy()
        _activityDetailStoryBinding = null
    }


}