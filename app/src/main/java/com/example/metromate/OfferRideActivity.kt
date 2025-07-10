package com.example.metromate

import android.app.DatePickerDialog
import android.app.TimePickerDialog
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

class OfferRideActivity : AppCompatActivity() {

    private lateinit var etFromLocation: AutoCompleteTextView
    private lateinit var etToLocation: AutoCompleteTextView
    private lateinit var etDate: EditText
    private lateinit var etTime: EditText
    private lateinit var etName: EditText
    private lateinit var etPhone: EditText
    private lateinit var etSeats: EditText

    private val db = FirebaseFirestore.getInstance()
    private val ridesCollection = db.collection("rides")
    private val auth = FirebaseAuth.getInstance()

    private val calendar = Calendar.getInstance()
    private val locationSuggestions = arrayOf(
        "BMSCE Front Gate", "BMSCE Hostel Gate", "BMSCE Law Gate", "National Clg metro side", "VV Puram metro Side",
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_offer_ride)

        etFromLocation = findViewById(R.id.etFromLocation)
        etToLocation = findViewById(R.id.etToLocation)
        etDate = findViewById(R.id.etDate)
        etTime = findViewById(R.id.etTime)
        etName = findViewById(R.id.etName)
        etPhone = findViewById(R.id.etPhone)
        etSeats = findViewById(R.id.etSeats)
        val btnSubmitRide = findViewById<Button>(R.id.btnSubmitRide)

        setupLocationDropdowns()

        setupDatePicker()
        setupTimePicker()
        prefillUserData()

        btnSubmitRide.setOnClickListener {
            submitRide()
        }
    }

    private fun setupLocationDropdowns() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, locationSuggestions)

        etFromLocation.setAdapter(adapter)
        etToLocation.setAdapter(adapter)
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

    private fun prefillUserData() {
        val currentUser = auth.currentUser
        currentUser?.let { user ->
            user.displayName?.let { name ->
                etName.setText(name)
            }
            user.phoneNumber?.let { phone ->
                etPhone.setText(phone)
            }
        }
    }

    private fun submitRide() {
        val fromLocation = etFromLocation.text.toString().trim()
        val toLocation = etToLocation.text.toString().trim()
        val date = etDate.text.toString().trim()
        val time = etTime.text.toString().trim()
        val name = etName.text.toString().trim()
        val phone = etPhone.text.toString().trim()
        val seats = etSeats.text.toString().trim()

        if (validateInput(fromLocation, toLocation, date, time, name, phone, seats)) {
            val seatsAvailable = seats.toInt()
            val timestamp = System.currentTimeMillis()
            val userId = auth.currentUser?.uid ?: ""

            val ride = Rides(
                userId = userId,
                fromLocation = fromLocation,
                toLocation = toLocation,
                date = date,
                time = time,
                name = name,
                phone = phone,
                seatsAvailable = seatsAvailable,
                timestamp = timestamp
            )

            ridesCollection.add(ride)
                .addOnSuccessListener {
                    Toast.makeText(this, "Ride submitted!", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun validateInput(
        fromLocation: String,
        toLocation: String,
        date: String,
        time: String,
        name: String,
        phone: String,
        seats: String
    ): Boolean {
        if (fromLocation.isEmpty()) {
            etFromLocation.error = "Please select starting location"
            return false
        }
        if (toLocation.isEmpty()) {
            etToLocation.error = "Please select destination"
            return false
        }
        if (date.isEmpty()) {
            etDate.error = "Please select date"
            return false
        }
        if (time.isEmpty()) {
            etTime.error = "Please select time"
            return false
        }
        if (name.isEmpty()) {
            etName.error = "Please enter your name"
            return false
        }
        if (phone.isEmpty()) {
            etPhone.error = "Please enter phone number"
            return false
        }
        if (seats.isEmpty()) {
            etSeats.error = "Please enter available seats"
            return false
        }
        if (seats.toIntOrNull() == null || seats.toInt() <= 0) {
            etSeats.error = "Please enter valid number of seats"
            return false
        }
        return true
    }
}