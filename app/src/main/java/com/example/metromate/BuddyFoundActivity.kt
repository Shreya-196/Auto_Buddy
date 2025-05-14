package com.example.metromate

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class BuddyFoundActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buddy_found)

        // Get the ride details from intent
        val ride = intent.getParcelableExtra<Rides>("ride")

        ride?.let {
            // Populate all ride details including name
            populateRideDetails(it)

            // Set up the call button
            setupCallButton(it)
        } ?: run {
            // Handle case where no ride was passed
            finish()
            Toast.makeText(this, "No ride details found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun populateRideDetails(ride: Rides) {
        // Update the greeting to include the buddy's name
        findViewById<TextView>(R.id.tvBuddy).text = "Travel Buddy: ${ride.name}"

        // Set all other ride details
        findViewById<TextView>(R.id.tvFromLocation).text = "From: ${ride.fromLocation}"
        findViewById<TextView>(R.id.tvToLocation).text = "To: ${ride.toLocation}"
        findViewById<TextView>(R.id.tvTime).text = "Time: ${ride.time}"
        findViewById<TextView>(R.id.tvSeatsAvailable).text = "Seats Available: ${ride.seatsAvailable}"
    }

    private fun setupCallButton(ride: Rides) {
        findViewById<Button>(R.id.btnCallNow).setOnClickListener {
            // Create phone dial intent
            val dialIntent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:${ride.phone}")
            }

            // Verify there's an app to handle the intent
            if (dialIntent.resolveActivity(packageManager) != null) {
                startActivity(dialIntent)
            } else {
                Toast.makeText(this, "No phone app available", Toast.LENGTH_SHORT).show()
            }
        }
    }
}