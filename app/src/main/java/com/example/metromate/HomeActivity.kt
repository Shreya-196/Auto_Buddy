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

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val tvWelcome = findViewById<TextView>(R.id.tvWelcome)

        if (currentUser != null) {
            val displayName = currentUser.displayName ?: currentUser.email ?: "User"
            tvWelcome.text = "Welcome, $displayName"
        } else {
            tvWelcome.text = "Welcome, Guest"
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        val offerRideCard = findViewById<CardView>(R.id.offerRide)
        offerRideCard.setOnClickListener {
            val intent = Intent(this, OfferRideActivity::class.java)
            startActivity(intent)
        }
        val findBuddyCard = findViewById<CardView>(R.id.findBuddy)
        findBuddyCard.setOnClickListener {
            val intent = Intent(this, FindABuddyActivity::class.java)
            startActivity(intent)
        }
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

        val btnLogout = findViewById<Button>(R.id.btnLogout)
        btnLogout.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }
}