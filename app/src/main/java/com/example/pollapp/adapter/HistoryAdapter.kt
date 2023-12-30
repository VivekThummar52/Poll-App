package com.example.pollapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pollapp.databinding.ItemCurrentPollsAdapterBinding
import com.example.pollapp.roomdbclasses.Poll
import com.example.pollapp.utils.PollDiffCallback

class HistoryAdapter(private val context: Context, private val pollHistoryList: ArrayList<Poll>, private val shouldEdit: Boolean) : ListAdapter<Poll, HistoryAdapter.ViewHolder>(PollDiffCallback()) {

    class ViewHolder(val binding: ItemCurrentPollsAdapterBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // previous binding was ItemHistoryAdapterBinding
        val binding = ItemCurrentPollsAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val adapterPosition = holder.adapterPosition
        val poll = pollHistoryList[adapterPosition]
        holder.binding.questionTextView.text = poll.question
        holder.binding.rvOption.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        val adapter = NestedOptionAdapter(poll.options, poll.anyOptionSelected, shouldEdit) {
            // this will never be called because answer selection is not possible in History list
        }
        holder.binding.rvOption.adapter = adapter
    }

    override fun getItemCount(): Int {
        return pollHistoryList.size
    }

    fun submitPolls(polls: List<Poll>?) {
        submitList(polls)
    }
}