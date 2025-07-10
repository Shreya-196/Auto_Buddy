package com.example.fragments

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.widget.Button

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
   override fun onCreate(savedInstanceState: Bundle?) {
       super.onCreate(savedInstanceState)
       setContentView(R.layout.activity_main)
//        lateinit var fragmentManager: FragmentManager
       var blueButton = findViewById<Button>(R.id.blueButton);
       var yellowButton = findViewById<Button>(R.id.yellowButton);
//        fragmentManager = getSupportFragmentManager();

       blueButton.setOnClickListener {
           loadFragment(fragment_1())
       }

       yellowButton.setOnClickListener {
           loadFragment(fragment_2())
       }
   }

   private fun loadFragment(fragment: Fragment) {
       // Use supportFragmentManager directly
       supportFragmentManager.beginTransaction()
           .replace(R.id.fragmentContainer, fragment)
           .addToBackStack(null)
           .commit()
   }
}