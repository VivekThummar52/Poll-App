package com.example.pollapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.pollapp.roomdbclasses.OptionListConverter

@Entity(tableName = "polls")
data class Poll(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val question: String,
    @TypeConverters(OptionListConverter::class)
    val options: List<Option>,
    var anyOptionSelected: Int = 0,
    var isPollAnswered: Int = 0
)
