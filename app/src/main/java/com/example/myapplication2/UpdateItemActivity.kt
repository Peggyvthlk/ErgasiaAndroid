package com.example.myapplication2

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication2.databinding.ActivityUpdateItemBinding

class UpdateItemActivity : BaseActivity() {

    private lateinit var binding: ActivityUpdateItemBinding
    private lateinit var db: ItemDatabaseHelper
    private var itemId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = ItemDatabaseHelper(this)

        itemId = intent.getIntExtra("item_id", -1)
        if (itemId == -1) {
            Toast.makeText(this, "Can't find item", Toast.LENGTH_SHORT).show()
            finish() // Exit activity if item ID is invalid
            return
        }

        // Button to refresh the current activity
        binding.editNoteHeading.setOnClickListener {
            val intent = Intent(this, UpdateItemActivity::class.java)
            startActivity(intent)
        }

        // Load existing item details into the UI
        val item = db.getItemById(itemId)
        if(item != null){
            binding.updateTitleEditText.setText(item.title)
            binding.updateContentEditText.setText(item.content)
            binding.UpdateSizeEditText.setText(item.size)
            binding.UpdateColorEditText.setText(item.color)
            binding.UpdateAvailabilityEditText.setText(item.availability)
            binding.UpdatePriceEditText.setText(item.price)
            binding.UpdateDiscountEditText.setText(item.discount)
            item.image?.let {
                binding.UpdateSelectedImageView.setImageBitmap(it)
                binding.UpdateSelectedImageView.visibility = ImageView.VISIBLE
            }
        }


        // Save button click listener to update the item
        binding.updateSaveButton.setOnClickListener {
            val newTitle = binding.updateTitleEditText.text.toString()
            val newContent = binding.updateContentEditText.text.toString()
            val updatedImage = (binding.UpdateSelectedImageView.drawable as? BitmapDrawable)?.bitmap
            val size = binding.UpdateSizeEditText.text.toString()
            val color = binding.UpdateColorEditText.text.toString()
            val availability = binding.UpdateAvailabilityEditText.text.toString()
            val price = binding.UpdatePriceEditText.text.toString()
            val discount = binding.UpdateDiscountEditText.text.toString()
            val updatedItem = Item(itemId, newTitle, newContent, updatedImage, size, color, availability, "", "", "", price, discount)
            db.updateItem(updatedItem)
            finish() // Close activity after saving changes
            Toast.makeText(this, "Changes Saved", Toast.LENGTH_SHORT).show()
        }

        // Button click listener to pick an image from the gallery
        binding.UpdateSelectImageButton.setOnClickListener {
            pickImageFromGallery()
        }
    }

    // Starts an activity to pick an image from the gallery
    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    // Handles the result of the image picker activity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            data?.data?.let { uri ->
                val bitmap = getBitmapFromUri(uri)
                if (bitmap != null) {
                    binding.UpdateSelectedImageView.setImageBitmap(bitmap)
                    binding.UpdateSelectedImageView.visibility = ImageView.VISIBLE
                } else {
                    Toast.makeText(this, "Unable to load image", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Converts image URI to Bitmap
    private fun getBitmapFromUri(uri: Uri): Bitmap? {
        return contentResolver.openInputStream(uri)?.use { inputStream ->
            BitmapFactory.decodeStream(inputStream)
        }
    }

    companion object {
        private const val PICK_IMAGE_REQUEST = 1 // Request code for image picking
    }
}
