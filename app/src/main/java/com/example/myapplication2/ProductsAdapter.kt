package com.example.myapplication2

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat.recreate
import androidx.recyclerview.widget.RecyclerView
import java.util.Locale

class ProductsAdapter(private var items: List<Item>, context: Context) :
    RecyclerView.Adapter<ProductsAdapter.ItemViewHolder>() {

    private val db: ItemDatabaseHelper = ItemDatabaseHelper(context)

    // ViewHolder class to hold item views
    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.allTitle)
        val productImage: ImageView = itemView.findViewById(R.id.allImageView)
        val price: TextView = itemView.findViewById(R.id.priceTextView)
        val rating: TextView = itemView.findViewById(R.id.ratingTextView)
        val discount: TextView = itemView.findViewById(R.id.discountTextView)
        val addToFavoritesBtn: Button = itemView.findViewById(R.id.addToFavorites)
        val addToCartBtn: Button = itemView.findViewById(R.id.addToShoplist)
        val ratingBar: RatingBar = itemView.findViewById(R.id.ratingBar)
        val categories: TextView = itemView.findViewById(R.id.ExtrasTextView)
        val updateButton: ImageView = itemView.findViewById(R.id.editImageView)
        val deleteButton: ImageView = itemView.findViewById(R.id.deleteImageView)
    }

    // Creates and returns a new ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.all_list_item, parent, false)
        return ItemViewHolder(view)
    }

    // Returns the number of items
    override fun getItemCount(): Int = items.size

    // Binds data to the ViewHolder
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]
        holder.title.text = item.title
        holder.productImage.setImageBitmap(item.image)
        holder.price.text = item.price.takeIf { it.isNotEmpty() } ?: "Price: Unknown"
        holder.rating.text = item.rating.takeIf { it.isNotEmpty() } ?: "Rating: Not rated"
        holder.discount.text = item.discount

        // Set extra info based on item size and color
        holder.categories.text = when {
            item.size.isNotEmpty() && item.color.isNotEmpty() -> "Extra info: ${item.size}, ${item.color}"
            item.size.isNotEmpty() -> "Extra info: ${item.size}"
            item.color.isNotEmpty() -> "Extra info: ${item.color}"
            else -> "Extra info: None"
        }

        // Set the rating bar if rating is not empty
        holder.ratingBar.rating = item.rating.toFloatOrNull() ?: 0f

        // Handle "Add to Favorites" button click
        holder.addToFavoritesBtn.setOnClickListener {
            db.addToFavorites(item)
            refreshData(db.getAllProducts())
            Toast.makeText(holder.itemView.context, "Added to Favorites", Toast.LENGTH_SHORT).show()
        }

        // Handle "Add to Cart" button click
        holder.addToCartBtn.setOnClickListener {
            db.addToCart(item)
            refreshData(db.getAllProducts())
            Toast.makeText(holder.itemView.context, "Added to Cart", Toast.LENGTH_SHORT).show()
        }

        // Handle "Update" button click
        holder.updateButton.setOnClickListener {
            val intent = Intent(holder.itemView.context, UpdateItemActivity::class.java).apply {
                putExtra("item_id", item.id)
            }
            holder.itemView.context.startActivity(intent)
        }

        // Handle "Delete" button click
        holder.deleteButton.setOnClickListener {
            db.deleteItem(item.id)
            refreshData(db.getAllItems())
            Toast.makeText(holder.itemView.context, "Item Deleted", Toast.LENGTH_SHORT).show()
        }

        // Handle rating changes
        holder.ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            db.updateRating(item, rating.toString())
            refreshData(db.getAllProducts())
            Toast.makeText(holder.itemView.context, "Rating: $rating", Toast.LENGTH_SHORT).show()
        }
    }

    // Updates the list of items and notifies adapter of the change
    fun refreshData(newItems: List<Item>) {
        items = newItems
        notifyDataSetChanged()
    }
}