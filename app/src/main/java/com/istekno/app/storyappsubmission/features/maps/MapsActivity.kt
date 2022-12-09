package com.istekno.app.storyappsubmission.features.maps

import android.Manifest
import android.content.ContentValues.TAG
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.istekno.app.core.data.source.remote.model.Story
import com.istekno.app.core.utils.AppUtils.isPermissionGranted
import com.istekno.app.core.utils.Dialog.cancelLoading
import com.istekno.app.core.utils.NetworkUtils.populateState
import com.istekno.app.storyappsubmission.R
import com.istekno.app.storyappsubmission.features.story.StoryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private val storyViewModel: StoryViewModel by viewModels()
    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        setupViews()
    }

    private fun setupViews() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync(this)
    }

    private fun getStoriesByLocation() {
        storyViewModel.getAllStory(1)
        observerStoriesByLocation()
    }

    private fun observerStoriesByLocation() {
        storyViewModel.storyData.observe(this) {
            populateState(
                it,
                onSuccess = {
                    cancelLoading()
                    it.data?.listStory?.let { it1 -> submitStoriesData(it1) }
                }
            )
        }
    }

    private fun submitStoriesData(listStory: List<Story.Response.Data>) {
        for (story in listStory) {
            val marker = MarkerOptions()
                .position(
                    LatLng(
                        story.lat ?: continue,
                        story.lon ?: continue
                    )
                )
                .title(story.name)
                .snippet(story.description)

            mMap.addMarker(marker)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        initMap(googleMap)
        getStoriesByLocation()
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) obtainMyLocation()
        }

    private fun obtainMyLocation() {
        if (isPermissionGranted(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun initMap(map: GoogleMap) {
        mMap = map

        obtainMyLocation()
        customMapStyle()

        val defaultMarker = LatLng(-6.1753924, 106.8271528)
        mMap.addMarker(
            MarkerOptions()
                .position(defaultMarker)
                .title("Monumen Nasional")
                .snippet("Gambir, Central Jakarta City, Jakarta")
        )
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(defaultMarker, 7f))
    }

    private fun customMapStyle() {
        try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", exception)
        }
    }
}