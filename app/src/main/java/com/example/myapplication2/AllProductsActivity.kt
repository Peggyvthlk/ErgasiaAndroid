package com.example.myapplication2

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication2.databinding.ActivityAllProductsBinding
import java.util.Locale
class AllProductsActivity : BaseActivity() {
    private lateinit var binding: ActivityAllProductsBinding // For binding layout views
    private lateinit var db: ItemDatabaseHelper // To handle database operations
    private lateinit var productsAdapter: ProductsAdapter // Adapter for displaying products
    private var selectedImage: Bitmap? = null // Optional: Currently not used

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate the layout and set it as the content view
        binding = ActivityAllProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = ItemDatabaseHelper(this) // Initialize the database helper
        productsAdapter = ProductsAdapter(db.getAllProducts(), this) // Set up the adapter

        // Set up RecyclerView with LinearLayoutManager and the adapter
        binding.allProductsRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.allProductsRecyclerView.adapter = productsAdapter

        // Listen for text changes in the search bar to filter products
        binding.searchBarAllProducts.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Filter products based on search query and update the adapter
                val filteredItems = db.searchItems(s.toString())
                productsAdapter.refreshData(filteredItems)
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // Button to navigate to FavoritesActivity
        binding.bottomFavBtn.setOnClickListener {
            val intent = Intent(this, FavoritesActivity::class.java)
            startActivity(intent)
        }

        // Button to navigate to BuylistActivity
        binding.bottomCartBtn.setOnClickListener {
            val intent = Intent(this, BuylistActivity::class.java)
            startActivity(intent)
        }

        // Button to navigate to AddActivity for adding new items
        binding.addButton.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
        }

        // Button to refresh the current activity
        binding.allProductsHeading.setOnClickListener {
            val intent = Intent(this, AllProductsActivity::class.java)
            startActivity(intent)
        }

        // Buttons to change the language and recreate the activity
        binding.grImage.setOnClickListener {
            setLanguage("el", this) // Set language to Greek
            recreate() // Restart activity to apply language change
        }
        binding.engImage.setOnClickListener {
            setLanguage("en", this) // Set language to English
            recreate() // Restart activity to apply language change
        }
    }

    // Method to set the app language and restart the activity
    private fun setLanguage(languageCode: String, context: Context) {
        val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        with(sharedPreferences.edit()) {
            putString("language", languageCode)
            apply()
        }

        // Update the locale in BaseActivity
        BaseActivity.dLocale = Locale(languageCode)

        // Restart the activity to apply the language change
        val intent = Intent(context, context::class.java)
        context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK))
    }

    override fun onResume() {
        super.onResume()
        // Refresh the product list in the adapter when the activity resumes
        productsAdapter.refreshData(db.getAllProducts())
    }
}
