package com.example.nammarastereporter.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reports")
data class ReportEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val ticketId: String,
    val issueType: String,
    val latitude: Double,
    val longitude: Double,
    val timestamp: String,
    val imagePath: String,
    val userName: String,
    val userPhone: String,
    val status: String = "Pending"
)
