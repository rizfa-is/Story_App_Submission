package com.istekno.app.storyappsubmission.features.story.add

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.istekno.app.core.data.source.remote.model.Story
import com.istekno.app.core.utils.Alert.showToast
import com.istekno.app.core.utils.AppUtils.createPartFromFile
import com.istekno.app.core.utils.AppUtils.getImageUriFromBitmap
import com.istekno.app.core.utils.AppUtils.isPermissionGranted
import com.istekno.app.core.utils.Dialog.dialog2
import com.istekno.app.core.utils.FileUtils.compressFile
import com.istekno.app.core.utils.FileUtils.uriToFile
import com.istekno.app.core.utils.NetworkUtils.populateState
import com.istekno.app.core.utils.extensions.Context.showImage
import com.istekno.app.core.utils.extensions.Views.navigateInto
import com.istekno.app.core.utils.extensions.Views.onCLick
import com.istekno.app.storyappsubmission.R
import com.istekno.app.storyappsubmission.base.BaseFragment
import com.istekno.app.storyappsubmission.databinding.FragmentAddStoryBinding
import com.istekno.app.storyappsubmission.features.story.StoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

@AndroidEntryPoint
class AddStoryFragment : BaseFragment<FragmentAddStoryBinding>() {

    override val layout: Int
        get() = R.layout.fragment_add_story

    private val storyViewModel: StoryViewModel by viewModels()

    private lateinit var requestCameraPermission: ActivityResultLauncher<Array<String>>
    private lateinit var requestIntentCamera: ActivityResultLauncher<Intent>
    private lateinit var requestIntentGallery: ActivityResultLauncher<Intent>
    private lateinit var imageFile: Uri
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val story by lazy { Story.Request() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupData()
        setupViews()
    }

    private fun setupData() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())

        observerAddStory()
        setupIntentRequest()
    }

    private fun setupViews() {
        binding.apply {
            btnGallery.onCLick { chooseFromGallery() }

            btnCamera.onCLick { takePhoto() }

            btnAddStory.onCLick {
                val file = uriToFile(imageFile, requireContext())
                val compressFile = compressFile(file)

                story.photo = createPartFromFile(compressFile)
                story.description = etDesc.text.toString().toRequestBody(MultipartBody.FORM)
                addStory()
            }

            cbAddLocation.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    obtainLastLocation()
                } else {
                    story.lat = null
                    story.lon = null
                }
            }
        }
    }

    private fun setupIntentRequest() {
        requestIntentGallery = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                try {
                    binding.ivPhoto.setImageURI(it.data?.data!!)
                    imageFile = it.data?.data!!
                } catch (e: Exception) {
                    e.printStackTrace()
                    requireContext().dialog2(e.message ?: "") { dialog ->
                        dialog.dismiss()
                    }
                }
            }
        }

        requestIntentCamera = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                try {
                    val bitmap = it.data?.extras?.get("data") as Bitmap
                    requireContext().showImage(bitmap, binding.ivPhoto)

                    imageFile = getImageUriFromBitmap(
                        requireContext(),
                        bitmap
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                    requireContext().dialog2(e.message ?: "") { dialog ->
                        dialog.dismiss()
                    }
                }
            }
        }

        requestCameraPermission = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.CAMERA] ?: false -> {
                    requestIntentCamera.launch(
                        Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    )
                }
                permissions[Manifest.permission.READ_EXTERNAL_STORAGE] ?: false -> {
                    requestIntentGallery.launch(
                        Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        )
                    )
                }
            }
        }
    }

    private fun addStory() {
        storyViewModel.addStory(story)
    }

    private fun observerAddStory() {
        storyViewModel.responseData.observe(viewLifecycleOwner) {
            requireActivity().apply {
                populateState(
                    it,
                    onSuccess = {
                        hideLoading()
                        dialog2(it.data?.message ?: "") {
                            it.dismiss()
                            requireView().navigateInto(R.id.action_addStoryFragment_to_storyFragment)
                        }
                    }
                )
            }
        }
    }

    private fun chooseFromGallery() {
        if (isPermissionGranted(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
        ) {
            requestIntentGallery.launch(
                Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )
            )
        } else {
            requestCameraPermission.launch(
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
            )
        }
    }

    private fun takePhoto() {
        if (isPermissionGranted(
                requireContext(),
                Manifest.permission.CAMERA)
        ) {
            requestIntentCamera.launch(
                Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            )
        } else {
            requestCameraPermission.launch(
                arrayOf(Manifest.permission.CAMERA)
            )
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true -> obtainLastLocation()
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true -> obtainLastLocation()
            }
        }

    private fun obtainLastLocation() {
        if (isPermissionGranted(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) &&
            isPermissionGranted(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    requireContext().showToast(
                        getString(R.string.success_get_location)
                    )

                    story.lat = location.latitude.toString().toRequestBody(MultipartBody.FORM)
                    story.lon = location.longitude.toString().toRequestBody(MultipartBody.FORM)
                } else {
                    requireContext().showToast(
                        getString(R.string.fail_get_location)
                    )

                    story.lat = null
                    story.lon = null
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

}