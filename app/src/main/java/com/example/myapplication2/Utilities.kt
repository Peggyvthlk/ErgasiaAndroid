package com.example.myapplication2

import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.myapplication2.ItemDatabaseHelper.Companion.COLUMN_AVAILABILITY
import com.example.myapplication2.ItemDatabaseHelper.Companion.COLUMN_BUYLIST
import com.example.myapplication2.ItemDatabaseHelper.Companion.COLUMN_COLOR
import com.example.myapplication2.ItemDatabaseHelper.Companion.COLUMN_CONTENT
import com.example.myapplication2.ItemDatabaseHelper.Companion.COLUMN_DISCOUNT
import com.example.myapplication2.ItemDatabaseHelper.Companion.COLUMN_FAVORITE
import com.example.myapplication2.ItemDatabaseHelper.Companion.COLUMN_ID
import com.example.myapplication2.ItemDatabaseHelper.Companion.COLUMN_IMAGE
import com.example.myapplication2.ItemDatabaseHelper.Companion.COLUMN_PRICE
import com.example.myapplication2.ItemDatabaseHelper.Companion.COLUMN_RATING
import com.example.myapplication2.ItemDatabaseHelper.Companion.COLUMN_SIZE
import com.example.myapplication2.ItemDatabaseHelper.Companion.COLUMN_TITLE
import java.io.ByteArrayOutputStream

class Utilities {
    // Utility method to convert a Bitmap to ByteArray
      fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    // Utility method to convert a ByteArray back to Bitmap
    private fun byteArrayToBitmap(byteArray: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }

    // Utility method to retrive an items list from database
    fun getItemsFromDatabase(cursor: Cursor):List<Item>{
        val list=mutableListOf<Item>()
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
            val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))
            val imageBytes = cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_IMAGE))
            val size = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SIZE))
            val color = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COLOR))
            val availability = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_AVAILABILITY))
            val image = imageBytes?.let { byteArrayToBitmap(it) }
            val discount = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DISCOUNT))
            val favorite = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FAVORITE))
            val price = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRICE))
            val buylist = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BUYLIST))
            val rating = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RATING))
            val item = Item(
                id,
                title,
                content,
                image,
                size,
                color,
                availability,
                buylist,
                favorite,
                rating,
                price,
                discount
            )
            list.add(item)
        }
        return list
    }

    // Utility method to retrieve a single item from database
    fun getItemFromDatabase(cursor: Cursor):Item{
        cursor.moveToFirst()

        val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
        val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
        val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))
        val imageBytes = cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_IMAGE))
        val size = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SIZE))
        val color = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COLOR))
        val availability = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_AVAILABILITY))
        val image = imageBytes?.let { byteArrayToBitmap(it) }
        val discount = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DISCOUNT))
        val favorite = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FAVORITE))
        val price = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRICE))
        val buylist = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BUYLIST))
        val rating = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RATING))

        return Item(
            id,
            title,
            content,
            image,
            size,
            color,
            availability,
            buylist,
            favorite,
            rating,
            price,
            discount
        )
    }
}