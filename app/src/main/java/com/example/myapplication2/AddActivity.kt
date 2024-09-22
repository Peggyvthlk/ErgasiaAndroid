package com.example.myapplication2

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication2.databinding.ActivityAddBinding

class AddActivity : BaseActivity() {

    private lateinit var binding: ActivityAddBinding // For binding layout views
    private lateinit var db: ItemDatabaseHelper // To handle database operations
    private var selectedImage: Bitmap? = null // To store the selected image

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate the layout and set it as the content view
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = ItemDatabaseHelper(this) // Initialize the database helper

        // Set up the button to select an image from the gallery
        binding.selectImageButton.setOnClickListener {
            pickImageFromGallery()
        }

        // Button to refresh the current activity
        binding.addNoteHeading.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
        }

        // Set up the button to save the item details
        binding.saveButton.setOnClickListener {
            // Retrieve text from input fields
            val title = binding.titleEditText.text.toString()
            val content = binding.contentEditText.text.toString()
            val size = binding.sizeEditText.text.toString()
            val color = binding.colorEditText.text.toString() // Fixed incorrect reference
            val availability = binding.availabilityEditText.text.toString()
            val price = binding.priceEditText.text.toString()
            val discount = binding.discountEditText.text.toString()

            // Check if title and content are not empty
            if (title.isNotBlank() && content.isNotBlank()) {
                // Create an item object and save it to the database
                val item = Item(0, title, content, selectedImage, size, color, availability, "", "", "", price, discount)
                db.insertItem(item)
                Toast.makeText(this, "Item Saved", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                // Show an error message if required fields are empty
                Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Method to pick an image from the gallery
    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    // Handle the result of the image picking intent
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                // Convert the image URI to a Bitmap and display it
                selectedImage = getBitmapFromUri(uri)
                binding.selectedImageView.setImageBitmap(selectedImage)
                binding.selectedImageView.visibility = ImageView.VISIBLE
            }
        }
    }

    // Convert a URI to a Bitmap depending on Android version
    private fun getBitmapFromUri(uri: Uri): Bitmap? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(contentResolver, uri)
            ImageDecoder.decodeBitmap(source)
        } else {
            MediaStore.Images.Media.getBitmap(contentResolver, uri)
        }
    }

    companion object {
        private const val PICK_IMAGE_REQUEST = 1 // Request code for picking an image
    }
}
