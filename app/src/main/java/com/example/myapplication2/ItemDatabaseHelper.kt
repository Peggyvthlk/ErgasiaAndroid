package com.example.myapplication2

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

/**
 * Helper class for managing the SQLite database, including table creation, upgrades, and CRUD operations.
 */
class ItemDatabaseHelper(context: Context) : SQLiteOpenHelper(
    context, DATABASE_NAME, null,
    DATABASE_VERSION
) {
    private val utilities = Utilities()
    private val context = context // Keep a reference to context for showing Toasts

    companion object {
        const val DATABASE_NAME = "itemsapp.db"  // Database file name
        const val DATABASE_VERSION = 11  // Database version for upgrades
        const val TABLE_NAME = "allitems"  // Table name

        // Column names
        const val COLUMN_ID = "id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_CONTENT = "content"
        const val COLUMN_IMAGE = "image"
        const val COLUMN_SIZE = "size"
        const val COLUMN_COLOR = "color"
        const val COLUMN_AVAILABILITY = "availability"
        const val COLUMN_RATING = "rating"
        const val COLUMN_FAVORITE = "favorite"
        const val COLUMN_BUYLIST = "buylist"
        const val COLUMN_PRICE = "price"
        const val COLUMN_DISCOUNT = "discount"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        try {
            // Creates the table with the specified columns
            val createTableQuery = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY," +
                    " $COLUMN_TITLE TEXT ," +
                    " $COLUMN_CONTENT TEXT," +
                    "$COLUMN_IMAGE BLOB," +
                    "$COLUMN_SIZE TEXT," +
                    "$COLUMN_COLOR TEXT," +
                    "$COLUMN_PRICE TEXT," +
                    "$COLUMN_DISCOUNT TEXT," +
                    "$COLUMN_RATING TEXT," +
                    "$COLUMN_FAVORITE BOOLEAN," +
                    "$COLUMN_BUYLIST BOOLEAN," +
                    "$COLUMN_AVAILABILITY TEXT)"
            db?.execSQL(createTableQuery)
        } catch (e: Exception) {
            Toast.makeText(context, "Error creating database: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        try {
            // Drops the old table and creates a new one for upgrades
            val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME"
            db?.execSQL(dropTableQuery)
            onCreate(db)
        } catch (e: Exception) {
            Toast.makeText(context, "Error upgrading database: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    fun insertItem(item: Item) {
        var db: SQLiteDatabase? = null
        try {
            db = writableDatabase
            val values = ContentValues().apply {
                put(COLUMN_TITLE, item.title)
                put(COLUMN_CONTENT, item.content)
                put(COLUMN_IMAGE, item.image?.let { utilities.bitmapToByteArray(it) })
                put(COLUMN_SIZE, item.size)
                put(COLUMN_COLOR, item.color)
                put(COLUMN_AVAILABILITY, item.availability)
                put(COLUMN_BUYLIST, item.buylist)
                put(COLUMN_DISCOUNT, item.discount)
                put(COLUMN_RATING, item.rating)
                put(COLUMN_FAVORITE, item.favoritesList)
                put(COLUMN_PRICE, item.price)
            }
            db.insert(TABLE_NAME, null, values)
        } catch (e: Exception) {
            Toast.makeText(context, "Error inserting item: ${e.message}", Toast.LENGTH_LONG).show()
        } finally {
            db?.close()
        }
    }

    // Retrieves all items from the database
    fun getAllItems(): List<Item> {
        val itemsList = mutableListOf<Item>()
        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null
        try {
            db = readableDatabase
            val query = "SELECT * FROM $TABLE_NAME"
            cursor = db.rawQuery(query, null)
            itemsList.addAll(utilities.getItemsFromDatabase(cursor))
        } catch (e: Exception) {
            Toast.makeText(context, "Error retrieving items: ${e.message}", Toast.LENGTH_LONG).show()
        } finally {
            cursor?.close()
            db?.close()
        }
        return itemsList
    }

    /*
    *   Getting products list used for BuylistActivity
    * */
    fun getAllCartItems(): List<Item> {
        val cartItems = mutableListOf<Item>()
        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null
        try {
            db = readableDatabase
            val query = "SELECT * FROM $TABLE_NAME WHERE TRIM($COLUMN_BUYLIST) = 'YES'"
            cursor = db.rawQuery(query, null)
            cartItems.addAll(utilities.getItemsFromDatabase(cursor))
        } catch (e: Exception) {
            Toast.makeText(context, "Error retrieving cart items: ${e.message}", Toast.LENGTH_LONG).show()
        } finally {
            cursor?.close()
            db?.close()
        }
        return cartItems
    }

    fun getAllFavorites(): List<Item> {
        val favoritesList = mutableListOf<Item>()
        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null
        try {
            db = readableDatabase
            val query = "SELECT * FROM $TABLE_NAME WHERE TRIM($COLUMN_FAVORITE) = 'YES'"
            cursor = db.rawQuery(query, null)
            favoritesList.addAll(utilities.getItemsFromDatabase(cursor))
        } catch (e: Exception) {
            Toast.makeText(context, "Error retrieving favorites: ${e.message}", Toast.LENGTH_LONG).show()
        } finally {
            cursor?.close()
            db?.close()
        }
        return favoritesList
    }

    /*
    *   Getting products list used for AllProductsActivity
    * */
    fun getAllProducts(): List<Item> {
        val productList = mutableListOf<Item>()
        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null
        try {
            db = readableDatabase
            val query = "SELECT * FROM $TABLE_NAME"
            cursor = db.rawQuery(query, null)
            productList.addAll(utilities.getItemsFromDatabase(cursor))
        } catch (e: Exception) {
            Toast.makeText(context, "Error retrieving products: ${e.message}", Toast.LENGTH_LONG).show()
        } finally {
            cursor?.close()
            db?.close()
        }
        return productList
    }

    // Updates an existing item's details
    fun updateItem(item: Item) {
        var db: SQLiteDatabase? = null
        try {
            db = writableDatabase
            val values = ContentValues().apply {
                put(COLUMN_TITLE, item.title)
                put(COLUMN_CONTENT, item.content)
                put(COLUMN_IMAGE, item.image?.let { utilities.bitmapToByteArray(it) })
                put(COLUMN_SIZE, item.size)
                put(COLUMN_COLOR, item.color)
                put(COLUMN_AVAILABILITY, item.availability)
                put(COLUMN_DISCOUNT, item.discount)
            }
            val whereClause = "$COLUMN_ID = ?"
            val whereArgs = arrayOf(item.id.toString())
            db.update(TABLE_NAME, values, whereClause, whereArgs)
        } catch (e: Exception) {
            Toast.makeText(context, "Error updating item: ${e.message}", Toast.LENGTH_LONG).show()
        } finally {
            db?.close()
        }
    }

    // Updates the rating of an item
    fun updateRating(item: Item, rating: String) {
        var db: SQLiteDatabase? = null
        try {
            db = writableDatabase
            val values = ContentValues().apply {
                put(COLUMN_RATING, rating)
            }
            val whereClause = "$COLUMN_ID = ?"
            val whereArgs = arrayOf(item.id.toString())
            db.update(TABLE_NAME, values, whereClause, whereArgs)
        } catch (e: Exception) {
            Toast.makeText(context, "Error updating rating: ${e.message}", Toast.LENGTH_LONG).show()
        } finally {
            db?.close()
        }
    }

    // Marks an item as in the cart
    fun addToCart(item: Item) {
        var db: SQLiteDatabase? = null
        try {
            db = writableDatabase
            val values = ContentValues().apply {
                put(COLUMN_BUYLIST, "YES")
                put(COLUMN_AVAILABILITY, "1")
            }
            val whereClause = "$COLUMN_ID = ?"
            val whereArgs = arrayOf(item.id.toString())
            db.update(TABLE_NAME, values, whereClause, whereArgs)
        } catch (e: Exception) {
            Toast.makeText(context, "Error adding item to cart: ${e.message}", Toast.LENGTH_LONG).show()
        } finally {
            db?.close()
        }
    }

    // Marks an item as a favorite
    fun addToFavorites(item: Item) {
        var db: SQLiteDatabase? = null
        try {
            db = writableDatabase
            val values = ContentValues().apply {
                put(COLUMN_FAVORITE, "YES")
            }
            val whereClause = "$COLUMN_ID = ?"
            val whereArgs = arrayOf(item.id.toString())
            db.update(TABLE_NAME, values, whereClause, whereArgs)
        } catch (e: Exception) {
            Toast.makeText(context, "Error adding item to favorites: ${e.message}", Toast.LENGTH_LONG).show()
        } finally {
            db?.close()
        }
    }

    // Increases the quantity of an item
    fun addQuantity(item: Item) {
        var db: SQLiteDatabase? = null
        try {
            db = writableDatabase
            val values = ContentValues().apply {
                put(COLUMN_AVAILABILITY, (item.availability.toInt() + 1).toString())
            }
            val whereClause = "$COLUMN_ID = ?"
            val whereArgs = arrayOf(item.id.toString())
            db.update(TABLE_NAME, values, whereClause, whereArgs)
        } catch (e: Exception) {
            Toast.makeText(context, "Error adding quantity: ${e.message}", Toast.LENGTH_LONG).show()
        } finally {
            db?.close()
        }
    }

    // Decreases the quantity of an item
    fun removeQuantity(item: Item) {
        if (item.availability.toInt() == 0) {
            return
        }
        var db: SQLiteDatabase? = null
        try {
            db = writableDatabase
            val values = ContentValues().apply {
                put(COLUMN_AVAILABILITY, (item.availability.toInt() - 1).toString())
            }
            val whereClause = "$COLUMN_ID = ?"
            val whereArgs = arrayOf(item.id.toString())
            db.update(TABLE_NAME, values, whereClause, whereArgs)
        } catch (e: Exception) {
            Toast.makeText(context, "Error removing quantity: ${e.message}", Toast.LENGTH_LONG).show()
        } finally {
            db?.close()
        }
    }

    // Removes an item from the cart
    fun removeFromCart(item: Item) {
        var db: SQLiteDatabase? = null
        try {
            db = writableDatabase
            val values = ContentValues().apply {
                put(COLUMN_BUYLIST, "NO")
            }
            val whereClause = "$COLUMN_ID = ?"
            val whereArgs = arrayOf(item.id.toString())
            db.update(TABLE_NAME, values, whereClause, whereArgs)
        } catch (e: Exception) {
            Toast.makeText(context, "Error removing item from cart: ${e.message}", Toast.LENGTH_LONG).show()
        } finally {
            db?.close()
        }
    }

    // Removes an item from favorites
    fun removeFromFavorites(item: Item) {
        var db: SQLiteDatabase? = null
        try {
            db = writableDatabase
            val values = ContentValues().apply {
                put(COLUMN_FAVORITE, "NO")
            }
            val whereClause = "$COLUMN_ID = ?"
            val whereArgs = arrayOf(item.id.toString())
            db.update(TABLE_NAME, values, whereClause, whereArgs)
        } catch (e: Exception) {
            Toast.makeText(context, "Error removing item from favorites: ${e.message}", Toast.LENGTH_LONG).show()
        } finally {
            db?.close()
        }
    }

    /*
    *  This method accepts an integer as a parameter, it then tries to find an item with
    * a item id equal to this integer. If an item is found then it gets returned.
    * */
    fun getItemById(itemId: Int): Item? {
        var item: Item? = null
        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null
        try {
            db = readableDatabase
            val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = ?"
            cursor = db.rawQuery(query, arrayOf(itemId.toString()))
            item = utilities.getItemFromDatabase(cursor)
        } catch (e: Exception) {
            Toast.makeText(context, "Error retrieving item by ID: ${e.message}", Toast.LENGTH_LONG).show()
        } finally {
            cursor?.close()
            db?.close()
        }
        return item
    }

    // Deletes an item by its ID
    fun deleteItem(itemId: Int) {
        var db: SQLiteDatabase? = null
        try {
            db = writableDatabase
            val whereClause = "$COLUMN_ID = ?"
            val whereArgs = arrayOf(itemId.toString())
            db.delete(TABLE_NAME, whereClause, whereArgs)
        } catch (e: Exception) {
            Toast.makeText(context, "Error deleting item: ${e.message}", Toast.LENGTH_LONG).show()
        } finally {
            db?.close()
        }
    }

    // Searches for items matching the keyword in title, content, size, or color
    fun searchItems(keyword: String): List<Item> {
        val itemsList = mutableListOf<Item>()
        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null
        try {
            db = readableDatabase
            // Use LIKE to perform search in both title and content columns
            val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_TITLE LIKE ? OR $COLUMN_CONTENT LIKE ? OR $COLUMN_SIZE LIKE ? OR $COLUMN_COLOR LIKE ?"
            val selectionArgs = arrayOf("%$keyword%", "%$keyword%", "%$keyword%", "%$keyword%")
            cursor = db.rawQuery(query, selectionArgs)
            itemsList.addAll(utilities.getItemsFromDatabase(cursor))
        } catch (e: Exception) {
            Toast.makeText(context, "Error searching items: ${e.message}", Toast.LENGTH_LONG).show()
        } finally {
            cursor?.close()
            db?.close()
        }
        return itemsList
    }
}
