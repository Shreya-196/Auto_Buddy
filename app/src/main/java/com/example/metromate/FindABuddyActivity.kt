package com.example.metromate

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class FindABuddyActivity : AppCompatActivity() {

    private lateinit var etFromLocation: AutoCompleteTextView
    private lateinit var etToLocation: AutoCompleteTextView
    private lateinit var etDate: EditText
    private lateinit var etTime: EditText
    private lateinit var btnFind: Button

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val calendar = Calendar.getInstance()

    private val locationSuggestions = arrayOf(
        "BMSCE Front Gate", "BMSCE Hostel Gate", "BMSCE Law Gate", "National Clg metro side", "VV Puram metro Side",
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_abuddy)

        // Initialize views
        etFromLocation = findViewById(R.id.etFromLocation)
        etToLocation = findViewById(R.id.etToLocation)
        etDate = findViewById(R.id.etDate)
        etTime = findViewById(R.id.etTime)
        btnFind = findViewById(R.id.btnFind)

        // Setup location dropdowns
        setupLocationDropdowns()

        // Setup date and time pickers
        setupDatePicker()
        setupTimePicker()

        // Set click listener for find button
        btnFind.setOnClickListener {
            searchForBuddies()
        }
    }

    private fun setupLocationDropdowns() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, locationSuggestions)

        etFromLocation.setAdapter(adapter)
        etToLocation.setAdapter(adapter)

        // Show suggestions after 1 character typed
        etFromLocation.threshold = 1
        etToLocation.threshold = 1
    }

    private fun setupDatePicker() {
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateInView()
        }

        etDate.setOnClickListener {
            DatePickerDialog(
                this,
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun setupTimePicker() {
        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)
            updateTimeInView()
        }

        etTime.setOnClickListener {
            TimePickerDialog(
                this,
                timeSetListener,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false
            ).show()
        }
    }

    private fun updateDateInView() {
        val myFormat = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
        etDate.setText(sdf.format(calendar.time))
    }

    private fun updateTimeInView() {
        val myFormat = "hh:mm a"
        val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
        etTime.setText(sdf.format(calendar.time))
    }

    private fun searchForBuddies() {
        val fromLocation = etFromLocation.text.toString().trim()
        val toLocation = etToLocation.text.toString().trim()
        val date = etDate.text.toString().trim()
        val time = etTime.text.toString().trim()

        if (fromLocation.isEmpty() || toLocation.isEmpty()) {
            Toast.makeText(this, "Please select both locations", Toast.LENGTH_SHORT).show()
            return
        }

        // Build query based on available filters
        var query = db.collection("rides")
            .whereEqualTo("fromLocation", fromLocation)
            .whereEqualTo("toLocation", toLocation)

        // Add date filter if provided
        if (date.isNotEmpty()) {
            query = query.whereEqualTo("date", date)
        }

        // Add time filter if provided
        if (time.isNotEmpty()) {
            query = query.whereEqualTo("time", time)
        }

        query.get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.isEmpty) {
                    Toast.makeText(this, "Uh oh! Couldnt find a buddy", Toast.LENGTH_SHORT).show()
                } else {
                    // Get all matching rides (you might want to implement sorting/filtering)
                    val rides = querySnapshot.documents.mapNotNull { it.toObject(Rides::class.java) }

                    // For now, just take the first ride
                    rides.firstOrNull()?.let { ride ->
                        openBuddyFound(ride)
                    }
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error searching for rides: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun openBuddyFound(ride: Rides) {
        val intent = Intent(this, BuddyFoundActivity::class.java).apply {
            putExtra("ride", ride)
        }
        startActivity(intent)
    }
}