package com.codingub.emergency.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Service")
data class ServiceEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val name: String,
    val phone: String,
    val language: String
)
