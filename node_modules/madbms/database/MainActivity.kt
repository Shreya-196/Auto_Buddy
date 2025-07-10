package com.example.database

import android.content.DialogInterface
import android.database.Cursor
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var etFirstName: EditText
    private lateinit var etLastName: EditText
    private lateinit var btnAddStudent: Button
    private lateinit var btnDeleteStudent: Button
    private lateinit var btnUpdateStudent: Button
    private lateinit var btnListStudents: Button
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etFirstName = findViewById(R.id.et_firstName)
        etLastName = findViewById(R.id.et_lastName)
        btnAddStudent = findViewById(R.id.btn_addStudent)
        btnDeleteStudent = findViewById(R.id.btn_deleteStudent)
        btnUpdateStudent = findViewById(R.id.btn_updateStudent)
        btnListStudents = findViewById(R.id.btn_listStudents)

        databaseHelper = DatabaseHelper(this)

        // Add Student
        btnAddStudent.setOnClickListener {
            val firstName = etFirstName.text.toString().trim()
            val lastName = etLastName.text.toString().trim()

            if (firstName.isEmpty() || lastName.isEmpty()) {
                Toast.makeText(this, "Please enter both First Name and Last Name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (databaseHelper.addStudent(firstName, lastName)) {
                Toast.makeText(this, "Student added successfully!", Toast.LENGTH_SHORT).show()
                etFirstName.text.clear()
                etLastName.text.clear()
            } else {
                Toast.makeText(this, "Failed to add student. Maybe student already exists or an error occurred.", Toast.LENGTH_SHORT).show()
            }
        }

        // Delete Student
        btnDeleteStudent.setOnClickListener {
            val firstName = etFirstName.text.toString().trim()

            if (firstName.isEmpty()) {
                Toast.makeText(this, "Please enter the First Name to delete", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (databaseHelper.deleteStudent(firstName)) {
                Toast.makeText(this, "Student deleted successfully!", Toast.LENGTH_SHORT).show()
                etFirstName.text.clear()
                etLastName.text.clear()
            } else {
                Toast.makeText(this, "Failed to delete student. Student not found or an error occurred.", Toast.LENGTH_SHORT).show()
            }
        }

        // Update Student
        btnUpdateStudent.setOnClickListener {
            val oldFirstName = etFirstName.text.toString().trim()

            if (oldFirstName.isEmpty()) {
                Toast.makeText(this, "Please enter the First Name of the student to update", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!databaseHelper.studentExists(oldFirstName)) {
                Toast.makeText(this, "Student with this First Name does not exist.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Show an alert dialog to get the new name
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Update Student Name")

            // Set up the input fields
            val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_update_student, null)
            val newFirstNameEt = dialogView.findViewById<EditText>(R.id.et_newFirstName)
            val newLastNameEt = dialogView.findViewById<EditText>(R.id.et_newLastName)
            builder.setView(dialogView)

            // Set up the buttons
            builder.setPositiveButton("Update") { dialog: DialogInterface, _: Int ->
                val newFirstName = newFirstNameEt.text.toString().trim()
                val newLastName = newLastNameEt.text.toString().trim()

                if (newFirstName.isEmpty() || newLastName.isEmpty()) {
                    Toast.makeText(this, "New First Name and Last Name cannot be empty.", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                if (databaseHelper.updateStudent(oldFirstName, newFirstName, newLastName)) {
                    Toast.makeText(this, "Student updated successfully!", Toast.LENGTH_SHORT).show()
                    etFirstName.text.clear()
                    etLastName.text.clear()
                } else {
                    Toast.makeText(this, "Failed to update student. An error occurred.", Toast.LENGTH_SHORT).show()
                }
            }
            builder.setNegativeButton("Cancel") { dialog: DialogInterface, _: Int ->
                dialog.cancel()
            }

            builder.show()
        }

        // List Students
        btnListStudents.setOnClickListener {
            val res = databaseHelper.getAllStudents()
            if (res == null || res.count == 0) {
                // Show message
                showMessage("Error", "No students found")
                return@setOnClickListener
            }

            val buffer = StringBuffer()
            while (res.moveToNext()) {
                buffer.append("ID: ${res.getString(0)}\n")
                buffer.append("First Name: ${res.getString(1)}\n")
                buffer.append("Last Name: ${res.getString(2)}\n\n")
            }

            // Show all data
            showMessage("Student Data", buffer.toString())
            res.close() // Close the cursor
        }
    }

    private fun showMessage(title: String, message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(true)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.show()
    }
}
