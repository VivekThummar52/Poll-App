package com.example.qfonapppractical.roomdbclasses

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PollDao {
    @Insert
    suspend fun insert(poll: Poll)

    @Query("SELECT * FROM polls")
    fun getAllPolls(): LiveData<List<Poll>>

    @Query("UPDATE polls SET isPollAnswered = :isPollAnswered WHERE id = :id")
    suspend fun updatePoll(id: Long, isPollAnswered: Int): Int
}
