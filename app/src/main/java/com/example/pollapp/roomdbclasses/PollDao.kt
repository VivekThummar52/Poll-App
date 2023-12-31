package com.example.pollapp.roomdbclasses

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.pollapp.data.Poll

@Dao
interface PollDao {

    @Query("SELECT * FROM polls")
    fun getAllPolls(): LiveData<List<Poll>>

    @Insert
    suspend fun insert(poll: Poll)

    @Update
    suspend fun updatePoll(poll: Poll): Int
}
