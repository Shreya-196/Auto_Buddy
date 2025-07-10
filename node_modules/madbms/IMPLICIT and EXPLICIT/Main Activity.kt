package com.example.implicit

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Explicit Intent
        val explicitButton = findViewById<Button>(R.id.ExplicitButton)
        explicitButton.setOnClickListener {
            Toast.makeText(this, "Explicit Intent", Toast.LENGTH_SHORT).show()
            val explicitIntent = Intent(this, secondactivity::class.java)
            startActivity(explicitIntent)
        }

        // Implicit Intent
        val url = "https://www.google.com"
        val ib = findViewById<Button>(R.id.B1)
        ib.setOnClickListener {
            val implicitIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(implicitIntent)
        }
    }
}