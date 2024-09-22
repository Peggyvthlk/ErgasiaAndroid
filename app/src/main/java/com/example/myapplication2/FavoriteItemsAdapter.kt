package com.example.myapplication2

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
/**
 * FavoriteItemsAdapter is used to display favorite items in a RecyclerView.
 *
 * Its main functions are:
 * 1. Show each favorite item with its title, content, and image.
 * 2. Handle the removal of items from the favorites list.
 *
 * Key components:
 * - `db`: A helper class for database operations.
 * - `ItemViewHolder`: Holds references to views for each item.
 * - `onCreateViewHolder()`: Creates and returns a new view holder for the items.
 * - `getItemCount()`: Returns the number of favorite items.
 * - `onBindViewHolder()`: Binds data to the view holder and sets up the removal button.
 * - `refreshData()`: Updates the list of favorite items and refreshes the display.
 */

class FavoriteItemsAdapter(private var items: List<Item>, context: Context) :
    RecyclerView.Adapter<FavoriteItemsAdapter.ItemViewHolder>() {

    private val db: ItemDatabaseHelper = ItemDatabaseHelper(context)

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cancel: ImageView = itemView.findViewById(R.id.cancelFavoriteBtn)
        val image :ImageView = itemView.findViewById(R.id.itemImageView)
        val title :TextView = itemView.findViewById(R.id.titleTextView)
        val content :TextView = itemView.findViewById(R.id.contentTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.simple_list, parent, false)
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]
        holder.title.text = item.title
        holder.content.text = item.content
        holder.image.setImageBitmap(item.image)

        holder.cancel.setOnClickListener {
            db.removeFromFavorites(item)
            refreshData(db.getAllFavorites())
            Toast.makeText(holder.itemView.context, "Removed "+ item.title +" from favorites", Toast.LENGTH_SHORT).show()
        }


    }

    fun refreshData(newItems: List<Item>) {
        items = newItems
        notifyDataSetChanged()
    }
}