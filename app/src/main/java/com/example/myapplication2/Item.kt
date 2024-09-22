package com.example.myapplication2

import android.graphics.Bitmap

data class Item(val id: Int,
                val title: String ,
                val content:String,
                val image:Bitmap?,
                val size: String,
                val color:String,
                val availability: String,
                val buylist: String,
                val favoritesList:String,
                val rating:String,
                val price:String,
                val discount: String
    )
