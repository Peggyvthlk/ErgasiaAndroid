package com.example.myapplication2

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication2.databinding.ActivityBuylistBinding
import com.example.myapplication2.databinding.ActivityFavoritesBinding
/**
 * FavoritesActivity displays the list of favorite items in a RecyclerView.
 *
 * Its main functions are:
 * 1. Initialize the adapter and set up the RecyclerView to show favorite items.
 * 2. Refresh the list of favorites when the activity resumes.
 *
 * Key components:
 * - `db`: Handles database operations for favorite items.
 * - `favoriteItemsAdapter`: Adapter to manage and display the list of favorites.
 * - `onCreate()`: Sets up the layout, initializes the adapter, and configures the RecyclerView.
 * - `onResume()`: Updates the favorite items list when the activity is resumed.
 */

class FavoritesActivity : BaseActivity() {
    private lateinit var binding: ActivityFavoritesBinding
    private lateinit var db : ItemDatabaseHelper
    private lateinit var favoriteItemsAdapter: FavoriteItemsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritesBinding.inflate( layoutInflater)
        setContentView(binding.root)

        db = ItemDatabaseHelper(this)
        favoriteItemsAdapter = FavoriteItemsAdapter(db.getAllFavorites(),this)

        binding.allCartItemsRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.allCartItemsRecyclerView.adapter = favoriteItemsAdapter

        // Button to refresh the current activity
        binding.favoritesHeading.setOnClickListener {
            val intent = Intent(this, FavoritesActivity::class.java)
            startActivity(intent)
        }


    }
    override fun onResume() {
        super.onResume()
        favoriteItemsAdapter.refreshData(db.getAllFavorites())
    }
}