package com.example.sharedpref

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val name = findViewById<EditText>(R.id.ed1)
        val password = findViewById<EditText>(R.id.ed2)
        val save = findViewById<Button>(R.id.b1)
        val load = findViewById<Button>(R.id.b2)
        val del = findViewById<Button>(R.id.b4)

        // Instantiate SharedPreferences
        val sharedPref = getSharedPreferences("addName", Context.MODE_PRIVATE)
        val edit = sharedPref.edit()

        // Save data
        save.setOnClickListener {
            edit.putString("name", name.text.toString())
            edit.putString("password", password.text.toString())
            edit.commit()
            Toast.makeText(this, "Data Saved", Toast.LENGTH_LONG).show()
        }

        // Load data
        load.setOnClickListener {
            val getName = sharedPref.getString("name", "default value")
            val getPass = sharedPref.getString("password", "default value")
            Toast.makeText(this, "$getName $getPass", Toast.LENGTH_LONG).show()
        }

        // Delete data
        del.setOnClickListener {
            edit.clear()
            edit.commit()
            Toast.makeText(this, "Data Cleared", Toast.LENGTH_SHORT).show()
        }
    }
}
