package com.example.metromate
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
@Parcelize
data class Rides(val userId: String = "",
                 val fromLocation: String = "",
                 val toLocation: String = "",
                 val date: String = "",
                 val time: String = "",
                 val name: String = "",
                 val phone: String = "",
                 val seatsAvailable: Int = 0,
                 val timestamp: Long = 0
): Parcelable
