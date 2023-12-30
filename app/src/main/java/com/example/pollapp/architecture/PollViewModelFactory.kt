package com.example.pollapp.architecture

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PollViewModelFactory(private val repository: PollRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PollViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PollViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
