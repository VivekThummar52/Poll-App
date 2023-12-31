package com.example.pollapp.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pollapp.activity.MainActivity
import com.example.pollapp.adapter.HistoryAdapter
import com.example.pollapp.architecture.PollViewModel
import com.example.pollapp.databinding.FragmentHistoryBinding
import com.example.pollapp.data.Poll

class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding
    private lateinit var context: Context

    private val pollHistoryList = ArrayList<Poll>()
    private lateinit var pollViewModel: PollViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.context = context
        pollViewModel = (requireActivity() as MainActivity).pollViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        prepareRecyclerView()
        return binding.root
    }

    private fun prepareRecyclerView() {
        binding.rvPollHistory.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        val adapter = HistoryAdapter(context, pollHistoryList, false)
        binding.rvPollHistory.adapter = adapter
        updateViewOnBlankList(pollHistoryList.isEmpty())

        pollViewModel.allPolls.observe(viewLifecycleOwner) { polls ->
            pollHistoryList.clear()
            for (poll in polls) {
                if (poll.isPollAnswered == 1) {
                    Log.e("HistoryFragment", "update poll check 3 observe: $poll")
                    pollHistoryList.add(poll)
                }
            }
            adapter.submitPolls(pollHistoryList)
            updateViewOnBlankList(pollHistoryList.isEmpty())
        }

        if (pollHistoryList.isEmpty()) {
            binding.rvPollHistory.visibility = View.GONE
            binding.tvHistory.visibility = View.VISIBLE
        }
    }

    private fun updateViewOnBlankList(isListEmpty: Boolean) {
        if (isListEmpty) {
            binding.rvPollHistory.visibility = View.GONE
            binding.tvHistory.visibility = View.VISIBLE
        } else {
            binding.rvPollHistory.visibility = View.VISIBLE
            binding.tvHistory.visibility = View.GONE
        }
    }
}