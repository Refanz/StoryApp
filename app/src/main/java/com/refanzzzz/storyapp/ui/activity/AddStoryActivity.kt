package com.refanzzzz.storyapp.ui.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.refanzzzz.storyapp.data.Result
import com.refanzzzz.storyapp.databinding.ActivityAddStoryBinding
import com.refanzzzz.storyapp.ui.viewmodel.StoryViewModel
import com.refanzzzz.storyapp.ui.viewmodel.ViewModelFactory
import com.refanzzzz.storyapp.util.Utils.getImageUri
import com.refanzzzz.storyapp.util.Utils.reduceFileImage
import com.refanzzzz.storyapp.util.Utils.uriToFile

class AddStoryActivity : AppCompatActivity(), OnClickListener {

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }

    private var _activityAddStoryBinding: ActivityAddStoryBinding? = null
    private val binding get() = _activityAddStoryBinding

    private lateinit var storyViewModel: StoryViewModel

    private var currentImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        storyViewModel = obtainViewModel(this@AddStoryActivity)

        binding?.let {
            it.btnCamera.setOnClickListener(this)
            it.btnGallery.setOnClickListener(this)
            it.btnUpload.setOnClickListener(this)
        }
    }

    private fun initBinding() {
        _activityAddStoryBinding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding?.root)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding?.btnCamera?.id -> {
                openCamera()
            }

            binding?.btnGallery?.id -> {
                openGallery()
            }

            binding?.btnUpload?.id -> {
                upload()
            }
        }
    }

    private fun showImagePreview() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding?.ivStoryPreview?.setImageURI(it)
        }
    }

    private fun upload() {

        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this@AddStoryActivity).reduceFileImage()
            val description = binding?.editTextDescStory?.text.toString()

            storyViewModel.addNewStory(imageFile, description)
                .observe(this@AddStoryActivity) { result ->
                    if (result != null) {
                        when (result) {
                            is Result.Loading -> {
                                showLoading(true)
                            }

                            is Result.Success -> {
                                showToast(result.data.message.toString())
                                showLoading(false)

                                val intent = Intent(this@AddStoryActivity, MainActivity::class.java)
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)
                                finish()
                            }

                            is Result.Error -> {
                                showToast(result.error)
                                showLoading(false)
                            }
                        }
                    }
                }
        } ?: showToast("Isi semua informasi!")
    }

    private fun openCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImagePreview()
        }
    }

    private fun openGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImagePreview()
        } else {
            Log.d("Photo Picker", "No Media Selected")
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Toast.makeText(this@AddStoryActivity, "Permission request granted", Toast.LENGTH_SHORT)
                .show()
        } else {
            Toast.makeText(this@AddStoryActivity, "Permission request denied", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun allPermissionsGranted() = ContextCompat.checkSelfPermission(
        this@AddStoryActivity,
        REQUIRED_PERMISSION
    ) == PackageManager.PERMISSION_GRANTED

    private fun obtainViewModel(activity: AppCompatActivity): StoryViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[StoryViewModel::class.java]
    }

    private fun showLoading(isLoading: Boolean) {
        binding?.progressBarAddStory?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this@AddStoryActivity, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()

        _activityAddStoryBinding = null
    }
}