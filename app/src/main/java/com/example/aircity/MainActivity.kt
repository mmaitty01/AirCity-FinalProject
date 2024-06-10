package com.example.aircity


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager

class MainActivity : AppCompatActivity() {
   override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       PreferenceManager.getDefaultSharedPreferences(this).apply {
           if (!getBoolean("first", false)) {
               showfirsttime();
       }
    }
}

    private fun showfirsttime() {
        val myIntent = Intent(this, SecondActivity::class.java)
        startActivity(myIntent)

        PreferenceManager.getDefaultSharedPreferences(this).edit().apply {
            putBoolean("first", true)
            apply()
        }
    }
}

