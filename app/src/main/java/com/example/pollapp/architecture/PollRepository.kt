package com.example.pollapp.architecture

import androidx.lifecycle.LiveData
import com.example.pollapp.roomdbclasses.Poll
import com.example.pollapp.roomdbclasses.PollDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PollRepository(private val pollDao: PollDao) {
    val allPolls: LiveData<List<Poll>> = pollDao.getAllPolls()

    suspend fun insert(poll: Poll) {
        withContext(Dispatchers.IO) {
            pollDao.insert(poll)
        }
    }

    suspend fun updatePoll(id: Long, isPollAnswered: Int): Int {
        return withContext(Dispatchers.IO) {
            pollDao.updatePoll(id, isPollAnswered)
        }
    }
}
