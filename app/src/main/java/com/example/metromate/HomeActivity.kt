package com.example.metromate


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class HomeActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Get current user
        val currentUser = auth.currentUser

        // Display the logged-in user's information
        val tvWelcome = findViewById<TextView>(R.id.tvWelcome)

        if (currentUser != null) {
            // User is signed in
            val displayName = currentUser.displayName ?: currentUser.email ?: "User"
            tvWelcome.text = "Welcome, $displayName"
        } else {
            // No user is signed in (shouldn't happen if you have proper auth flow)
            tvWelcome.text = "Welcome, Guest"
            // Optionally redirect to login
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        // Offer a Ride Card - opens OfferARideActivity
        val offerRideCard = findViewById<CardView>(R.id.offerRide)
        offerRideCard.setOnClickListener {
            val intent = Intent(this, OfferRideActivity::class.java)
            startActivity(intent)
        }

        // Find a Buddy Card - opens FindBuddyActivity
        val findBuddyCard = findViewById<CardView>(R.id.findBuddy)
        findBuddyCard.setOnClickListener {
            val intent = Intent(this, FindABuddyActivity::class.java)
            startActivity(intent)
        }

        // Book Auto Card - deep links to Ola app
        val bookAutoCard = findViewById<CardView>(R.id.bookAuto)
        bookAutoCard.setOnClickListener {
            try {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse("olacabs://app/launch?")
                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(this, "Ola app not installed", Toast.LENGTH_SHORT).show()
                val playStoreIntent = Intent(Intent.ACTION_VIEW)
                playStoreIntent.data = Uri.parse("market://details?id=com.olacabs.customer")
                startActivity(playStoreIntent)
            }
        }

        // Logout Button
        val btnLogout = findViewById<Button>(R.id.btnLogout)
        btnLogout.setOnClickListener {
            // Sign out from Firebase
            auth.signOut()

            // Return to LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }
}