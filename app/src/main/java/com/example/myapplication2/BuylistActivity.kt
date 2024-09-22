package com.example.myapplication2

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication2.databinding.ActivityBuylistBinding

class BuylistActivity : BaseActivity() {
    private lateinit var binding: ActivityBuylistBinding // For binding layout views
    private lateinit var db: ItemDatabaseHelper // To handle database operations
    private lateinit var cartItemsAdapter: CartItemsAdapter // Adapter for RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuylistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = ItemDatabaseHelper(this)
        cartItemsAdapter = CartItemsAdapter(db.getAllCartItems(), this)

        // Set up RecyclerView with a LinearLayoutManager and the adapter
        binding.allCartItemsRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.allCartItemsRecyclerView.adapter = cartItemsAdapter

        // Button to refresh the current activity
        binding.buylistHeading.setOnClickListener {
            val intent = Intent(this, BuylistActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()
        // Update the adapter with the latest data from the database
        cartItemsAdapter.refreshData(db.getAllCartItems())
    }
}
