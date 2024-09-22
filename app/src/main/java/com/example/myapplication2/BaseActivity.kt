package com.example.myapplication2

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale


/**
 * BaseActivity is a base class for activities that need to handle language changes.
 *
 * Its main functions are:
 * 1. Apply the chosen language settings to the activity.
 * 2. Ensure the language updates when the activity is resumed.
 *
 * Key parts:
 * - `dLocale`: Stores the current language setting.
 * - `attachBaseContext()`: Sets the language when the activity's context is created.
 * - `onResume()`: Updates the language when the activity comes back to the screen.
 * - `updateBaseContextLocale()`: Applies the language settings to the activity's configuration.
 */

open class BaseActivity : AppCompatActivity() {

    companion object {
        var dLocale: Locale? = null
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(updateBaseContextLocale(newBase))
    }

    override fun onResume() {
        super.onResume()
        updateBaseContextLocale(this)
    }

    private fun updateBaseContextLocale(context: Context): Context {
        if (dLocale == null) {
            dLocale = Locale.getDefault() // Default to the system's current locale if not set
        }

        Locale.setDefault(dLocale)
        val config = Configuration(context.resources.configuration)
        config.setLocale(dLocale)

        return context.createConfigurationContext(config)
    }
}
