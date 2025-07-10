package com.example.sharedpref2

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText


class MainActivity : AppCompatActivity() {

    private lateinit var name: EditText
    private lateinit var age: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        name = findViewById(R.id.ed1)
        age = findViewById(R.id.ed2)
    }

    // Fetch the stored data in onResume()
    // Because this is what will be called when the app opens again
    override fun onResume() {
        super.onResume()

        // Fetching the stored data from SharedPreferences
        val sh = getSharedPreferences("MySharedPref", MODE_PRIVATE)

        val s1 = sh.getString("name", "")
        val a = sh.getInt("age", 0)

        // Setting the fetched data in the EditTexts
        name.setText(s1)
        age.setText(a.toString())
    }

    // Store the data in SharedPreferences in the onPause()
    // This method is called when the user closes the application
    override fun onPause() {
        super.onPause()

        // Creating a SharedPreferences object
        val sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
        val myEdit = sharedPreferences.edit()

        // Writing user data to SharedPreferences
        myEdit.putString("name", name.text.toString())

        // Safely parsing age input
        val ageText = age.text.toString()
        val ageInt = ageText.toIntOrNull() ?: 0
        myEdit.putInt("age", ageInt)

        // Applying changes
        myEdit.apply()
    }
}