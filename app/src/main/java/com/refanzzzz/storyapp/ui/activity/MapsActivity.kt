package com.refanzzzz.storyapp.ui.activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.refanzzzz.storyapp.R
import com.refanzzzz.storyapp.data.Result
import com.refanzzzz.storyapp.databinding.ActivityMapsBinding
import com.refanzzzz.storyapp.ui.viewmodel.MapsViewModel
import com.refanzzzz.storyapp.ui.viewmodel.ViewModelFactory

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    private var _activityMapsBinding: ActivityMapsBinding? = null
    private val binding get() = _activityMapsBinding

    private lateinit var mapsViewModel: MapsViewModel

    private val boundsBuilder = LatLngBounds.Builder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _activityMapsBinding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        mapsViewModel = obtainViewModel(this)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        addManyMarker()
    }

    private fun addManyMarker() {
        mapsViewModel.getStoriesWithLocation().observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {}

                    is Result.Success -> {
                        result.data.listStory.forEach { story ->

                            Log.d("MapsActivity", story.toString())

                            val latLng = LatLng(story.lat ?: 0.0, story.lon ?: 0.0)
                            mMap.addMarker(
                                MarkerOptions().position(latLng).title(story.name)
                                    .snippet(story.description)
                            )
                            boundsBuilder.include(latLng)
                        }

                        val bounds: LatLngBounds = boundsBuilder.build()
                        mMap.animateCamera(
                            CameraUpdateFactory.newLatLngBounds(
                                bounds,
                                resources.displayMetrics.widthPixels,
                                resources.displayMetrics.heightPixels,
                                300
                            )
                        )
                    }

                    is Result.Error -> {
                        showToast(result.error)
                    }
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun obtainViewModel(activity: AppCompatActivity): MapsViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[MapsViewModel::class.java]
    }

    override fun onDestroy() {
        super.onDestroy()
        _activityMapsBinding = null
    }
}