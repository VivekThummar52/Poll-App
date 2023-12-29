package com.example.qfonapppractical.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "options")
data class Option(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val text: String,
    var progress: Int = 0
)