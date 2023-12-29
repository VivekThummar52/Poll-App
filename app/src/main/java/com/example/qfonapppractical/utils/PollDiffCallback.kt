package com.example.qfonapppractical.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.qfonapppractical.roomdbclasses.Poll

class PollDiffCallback : DiffUtil.ItemCallback<Poll>() {
    override fun areItemsTheSame(oldItem: Poll, newItem: Poll): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Poll, newItem: Poll): Boolean {
        return oldItem == newItem
    }
}