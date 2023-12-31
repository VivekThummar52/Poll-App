package com.example.pollapp.architecture

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pollapp.data.Poll
import kotlinx.coroutines.launch

class PollViewModel(private val repository: PollRepository) : ViewModel() {
    val allPolls: LiveData<List<Poll>> = repository.allPolls

    fun insert(poll: Poll) {
        viewModelScope.launch {
            repository.insert(poll)
        }
    }

    suspend fun updatePoll(poll: Poll): Int {
        return repository.updatePoll(poll)
    }
}
