package com.refanzzzz.storyapp.ui.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.refanzzzz.storyapp.data.remote.response.ListStoryItem
import com.refanzzzz.storyapp.databinding.FragmentStoryBinding
import com.refanzzzz.storyapp.model.Story
import com.refanzzzz.storyapp.ui.activity.DetailStoryActivity
import com.refanzzzz.storyapp.ui.adapter.ListStoryAdapter
import com.refanzzzz.storyapp.ui.adapter.LoadingStateAdapter
import com.refanzzzz.storyapp.ui.viewmodel.StoryViewModel
import com.refanzzzz.storyapp.ui.viewmodel.ViewModelFactory

class StoryFragment : Fragment() {

    private var _binding: FragmentStoryBinding? = null
    private val binding get() = _binding

    private lateinit var storyViewModel: StoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStoryBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        storyViewModel = obtainViewModel(activity as AppCompatActivity)

        initRecyclerView()

        setRecyclerViewData()
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext())
        binding?.rvStory?.layoutManager = layoutManager
    }


    private fun setRecyclerViewData() {
        val adapter = ListStoryAdapter()
        binding?.rvStory?.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )

        binding?.rvStory?.adapter = adapter

        storyViewModel.story.observe(viewLifecycleOwner) {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        adapter.setOnItemClickCallback(object : ListStoryAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ListStoryItem, itemView: View) {
                val story = Story(data.name, data.description, data.photoUrl, data.createdAt)

                val intent = Intent(requireActivity(), DetailStoryActivity::class.java)
                intent.putExtra(STORY_KEY, story)
                startActivity(
                    intent,
                    ActivityOptionsCompat.makeSceneTransitionAnimation(itemView.context as Activity)
                        .toBundle()
                )
            }
        })
    }

    private fun obtainViewModel(activity: AppCompatActivity): StoryViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[StoryViewModel::class.java]
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val STORY_KEY = "story_key"
    }
}