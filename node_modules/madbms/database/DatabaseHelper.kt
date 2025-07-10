package com.example.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "StudentDB.db"
        private const val DATABASE_VERSION = 1

        const val TABLE_STUDENTS = "students"
        const val COL_ID = "id"
        const val COL_FIRST_NAME = "first_name"
        const val COL_LAST_NAME = "last_name"

        // SQL query to create the table
        private const val CREATE_TABLE_STUDENTS =
            "CREATE TABLE $TABLE_STUDENTS (" +
                    "$COL_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$COL_FIRST_NAME TEXT," +
                    "$COL_LAST_NAME TEXT" +
                    ")"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE_STUDENTS)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS $TABLE_STUDENTS")
        // Create tables again
        onCreate(db)
    }

    // --- CRUD Operations ---

    // Add a new student
    fun addStudent(firstName: String, lastName: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COL_FIRST_NAME, firstName)
            put(COL_LAST_NAME, lastName)
        }

        val result = db.insert(TABLE_STUDENTS, null, values)
        db.close()
        return result != -1L // Returns true if data inserted successfully, false otherwise
    }

    // Delete a student by first name
    fun deleteStudent(firstName: String): Boolean {
        val db = this.writableDatabase
        val result = db.delete(
            TABLE_STUDENTS,
            "$COL_FIRST_NAME = ?",
            arrayOf(firstName)
        )
        db.close()
        return result > 0 // Returns true if one or more rows were deleted
    }

    // Update a student's name
    fun updateStudent(oldFirstName: String, newFirstName: String, newLastName: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COL_FIRST_NAME, newFirstName)
            put(COL_LAST_NAME, newLastName)
        }

        val result = db.update(
            TABLE_STUDENTS,
            values,
            "$COL_FIRST_NAME = ?",
            arrayOf(oldFirstName)
        )
        db.close()
        return result > 0 // Returns true if one or more rows were updated
    }

    // Get all students
    fun getAllStudents(): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_STUDENTS", null)
    }

    // Check if a student exists by first name
    fun studentExists(firstName: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_STUDENTS,
            arrayOf(COL_ID),
            "$COL_FIRST_NAME = ?",
            arrayOf(firstName),
            null, null, null
        )
        val exists = cursor.count > 0
        cursor.close()
        db.close()
        return exists
    }
}