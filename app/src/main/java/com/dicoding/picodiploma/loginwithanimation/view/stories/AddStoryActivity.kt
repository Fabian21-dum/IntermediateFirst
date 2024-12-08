package com.dicoding.picodiploma.loginwithanimation.view.stories

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityAddStoryBinding
import com.dicoding.picodiploma.loginwithanimation.getImageUri
import com.dicoding.picodiploma.loginwithanimation.reduceFileImage
import com.dicoding.picodiploma.loginwithanimation.uriToFile
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.main.MainActivity
import com.dicoding.picodiploma.loginwithanimation.view.main.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class AddStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStoryBinding

    private val viewModel by viewModels<MainViewModel> { ViewModelFactory.getInstance(this) }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            binding.ivPicture.setImageURI(uri)
            viewModel.setCurrentImageUri(uri)
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            // The URI is already set in the viewModel, so we can immediately update the image
            val uri = viewModel.currentImageUri.value
            if (uri != null) {
                binding.ivPicture.setImageURI(uri) // Update the image view immediately
            } else {
                Toast.makeText(this, "Failed to capture image", Toast.LENGTH_SHORT).show()
            }
        } else {
            // If capture failed, set the image URI to null and clear the ImageView
            viewModel.setCurrentImageUri(null) // Reset URI in ViewModel
            binding.ivPicture.setImageURI(null) // Optionally clear the image view
            Toast.makeText(this, "Failed to capture image", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.show()
        supportActionBar?.title = getString(R.string.add_story)

        viewModel.currentImageUri.observe(this) {
            // This observer updates the image view whenever the URI changes
            binding.ivPicture.setImageURI(it)
        }

        binding.btnGals.setOnClickListener { startGallery() }
        binding.btnCams.setOnClickListener { startCamera() }
        binding.BtnSubmit.setOnClickListener { uploadStory() }
    }

    private fun uploadStory() {
        viewModel.currentImageUri.value?.let { uri ->
            lifecycleScope.launch {
                binding.progressBar.visibility = View.VISIBLE

                val imageFile = withContext(Dispatchers.IO) {
                    uriToFile(uri, this@AddStoryActivity).reduceFileImage()
                }

                Log.d("Image File", "showImage: ${imageFile.path}")
                val description = binding.descTextEdit.text.toString()

                viewModel.addStory(imageFile, description).observe(this@AddStoryActivity) { result ->
                    when (result) {
                        is com.dicoding.picodiploma.loginwithanimation.data.Result.Error -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(this@AddStoryActivity, result.error, Toast.LENGTH_SHORT).show()
                        }
                        is com.dicoding.picodiploma.loginwithanimation.data.Result.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is com.dicoding.picodiploma.loginwithanimation.data.Result.Success -> {
                            binding.progressBar.visibility = View.GONE
                            if (result.data.error == true) {
                                Toast.makeText(this@AddStoryActivity, result.data.message, Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this@AddStoryActivity, result.data.message, Toast.LENGTH_SHORT).show()
                                val intent = Intent(this@AddStoryActivity, MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)
                            }
                        }
                    }
                }
            }
        } ?: Toast.makeText(this, getString(R.string.add_error), Toast.LENGTH_SHORT).show()
    }

    private fun startCamera() {
        val imageUri = getImageUri(this)

        // Log the file path before launching the camera intent
        val file = File(imageUri.path) // Adjust this based on how the URI is handled
        Log.d("FilePath", "Image URI path: ${file.absolutePath}")
        if (file.exists()) {
            Log.d("FileExists", "File already exists!")
        } else {
            Log.d("FileExists", "File does not exist.")
        }

        viewModel.setCurrentImageUri(imageUri)
        launcherIntentCamera.launch(imageUri)
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }
}