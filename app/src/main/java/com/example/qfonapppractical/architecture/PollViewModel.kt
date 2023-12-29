package com.example.qfonapppractical.architecture

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qfonapppractical.roomdbclasses.Poll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PollViewModel(private val repository: PollRepository) : ViewModel() {
    val allPolls: LiveData<List<Poll>> = repository.allPolls

    fun insert(poll: Poll) {
        viewModelScope.launch {
            repository.insert(poll)
        }
    }

    suspend fun updatePoll(poll: Poll): Int {
        return withContext(Dispatchers.IO) {
            repository.updatePoll(poll.id, poll.isPollAnswered)
        }
    }
}
