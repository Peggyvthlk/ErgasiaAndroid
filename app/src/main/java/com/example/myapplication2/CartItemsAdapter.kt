package com.example.myapplication2

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.recyclerview.widget.RecyclerView
/**
 * CartItemsAdapter is used to display items in a cart within a RecyclerView.
 *
 * Its main tasks are:
 * 1. Show each item with its title, image, and quantity.
 * 2. Handle user actions like updating, deleting, or changing the quantity of items.
 *
 * Key components:
 * - `db`: A helper to interact with the database.
 * - `ItemViewHolder`: Holds references to the views for each item in the list.
 * - `onCreateViewHolder()`: Creates and returns a new view holder.
 * - `getItemCount()`: Returns the number of items in the list.
 * - `onBindViewHolder()`: Binds data to each view holder and sets up click listeners.
 * - `refreshData()`: Updates the list of items and refreshes the display.
 */

class CartItemsAdapter(private var items: List<Item>, context: Context) :
    RecyclerView.Adapter<CartItemsAdapter.ItemViewHolder>() {

    private val db: ItemDatabaseHelper = ItemDatabaseHelper(context)

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val updateButton: ImageView = itemView.findViewById(R.id.updateButton)
        val deleteButton: ImageView = itemView.findViewById(R.id.deleteButton)
        val addQuantity :ImageView = itemView.findViewById(R.id.addQuantityButton)
        val removeQuantityButton :ImageView = itemView.findViewById(R.id.removeQuantityButton)
        val removeFromCart :ImageView = itemView.findViewById(R.id.cancelButton)
        val quantity :TextView = itemView.findViewById(R.id.quantityTextView)
        val image:ImageView = itemView.findViewById(R.id.itemImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_item, parent, false)
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]
        holder.titleTextView.text = item.title
        holder.quantity.text = "Quantity: "+item.availability
        holder.image.setImageBitmap(item.image)
        holder.updateButton.setOnClickListener {
            val intent = Intent(holder.itemView.context, UpdateItemActivity::class.java).apply {
                putExtra("item_id", item.id)
            }
            holder.itemView.context.startActivity(intent)
        }

        holder.deleteButton.setOnClickListener {
            db.deleteItem(item.id)
            refreshData(db.getAllCartItems())
            Toast.makeText(holder.itemView.context, "Item Deleted", Toast.LENGTH_SHORT).show()
        }

        holder.addQuantity.setOnClickListener{
            db.addQuantity(item)
            refreshData(db.getAllCartItems())
        }
        holder.removeQuantityButton.setOnClickListener{
            db.removeQuantity(item)
            refreshData(db.getAllCartItems())
        }
        holder.removeFromCart.setOnClickListener {
            db.removeFromCart(item)
            refreshData(db.getAllCartItems())
        }
    }

    fun refreshData(newItems: List<Item>) {
        items = newItems
        notifyDataSetChanged()
    }
}
