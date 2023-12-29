package com.example.qfonapppractical.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.qfonapppractical.databinding.ItemCurrentPollsAdapterBinding
import com.example.qfonapppractical.roomdbclasses.Poll
import com.example.qfonapppractical.utils.PollDiffCallback

class CurrentPollAdapter(
    private val context: Context,
    private val pollList: ArrayList<Poll>,
    private val shouldEdit: Boolean,
    private val onOptionSelected: (position: Int) -> Unit
) : ListAdapter<Poll, CurrentPollAdapter.ViewHolder>(PollDiffCallback()) {

    class ViewHolder(val binding: ItemCurrentPollsAdapterBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCurrentPollsAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val adapterPosition = holder.adapterPosition
        val poll = pollList[adapterPosition]
        holder.binding.questionTextView.text = poll.question
        holder.binding.rvOption.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        val adapter = NestedOptionAdapter(poll.options, poll.anyOptionSelected, shouldEdit) {
            if (poll.anyOptionSelected == 0) {
                poll.anyOptionSelected = 1
                onOptionSelected(adapterPosition)
            }
        }
        holder.binding.rvOption.adapter = adapter
    }

    override fun getItemCount(): Int {
        return pollList.size
    }

    fun submitPolls(polls: List<Poll>) {
        submitList(polls)
    }
}