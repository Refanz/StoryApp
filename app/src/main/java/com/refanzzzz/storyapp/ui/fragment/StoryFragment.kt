package com.refanzzzz.storyapp.ui.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.refanzzzz.storyapp.data.Result
import com.refanzzzz.storyapp.data.model.Story
import com.refanzzzz.storyapp.data.remote.response.ListStoryItem
import com.refanzzzz.storyapp.databinding.FragmentStoryBinding
import com.refanzzzz.storyapp.ui.activity.DetailStoryActivity
import com.refanzzzz.storyapp.ui.adapter.ListStoryAdapter
import com.refanzzzz.storyapp.ui.viewmodel.StoryViewModel
import com.refanzzzz.storyapp.ui.viewmodel.ViewModelFactory

class StoryFragment : Fragment() {

    companion object {
        const val STORY_KEY = "story_key"
    }

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

        storyViewModel.getAllStory().observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        showLoading(true)
                    }

                    is Result.Success -> {
                        @Suppress("UNCHECKED_CAST")
                        setRecyclerViewData(result.data.listStory as List<ListStoryItem>)
                        showLoading(false)
                    }

                    is Result.Error -> {
                        Toast.makeText(
                            requireContext(),
                            "Data tidak ditemukan!",
                            Toast.LENGTH_SHORT
                        ).show()
                        showLoading(false)
                    }
                }
            }
        }
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext())
        binding?.rvStory?.layoutManager = layoutManager
    }


    private fun setRecyclerViewData(listStory: List<ListStoryItem>) {
        val adapter = ListStoryAdapter(listStory as ArrayList<ListStoryItem>)
        binding?.rvStory?.adapter = adapter

        adapter.setOnItemClickCallback(object : ListStoryAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ListStoryItem, itemView: View) {
                val story = Story(data.name, data.description, data.photoUrl, data.createdAt)

                val intent = Intent(requireActivity(), DetailStoryActivity::class.java)
                intent.putExtra(STORY_KEY, story)
                startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(itemView.context as Activity).toBundle())
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding?.progressBarListStory?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun obtainViewModel(activity: AppCompatActivity): StoryViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[StoryViewModel::class.java]
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}