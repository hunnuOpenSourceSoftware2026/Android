package com.example.inspectionlab4

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "inspection_records")
data class InspectionRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val taskId: String,
    val deviceName: String,
    val location: String,
    val inspectorName: String,
    val recordedAt: Long = System.currentTimeMillis(),
)

