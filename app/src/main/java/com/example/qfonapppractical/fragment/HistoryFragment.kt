package com.example.vivekpracticalnov1.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vivekpracticalnov1.activity.MainActivity
import com.example.vivekpracticalnov1.adapter.HistoryAdapter
import com.example.vivekpracticalnov1.architecture.PollRepository
import com.example.vivekpracticalnov1.architecture.PollViewModel
import com.example.vivekpracticalnov1.architecture.PollViewModelFactory
import com.example.vivekpracticalnov1.databinding.FragmentHistoryBinding
import com.example.vivekpracticalnov1.roomdbclasses.AppDatabase
import com.example.vivekpracticalnov1.roomdbclasses.Poll

class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding
    private lateinit var context: Context

    private val pollHistoryList = ArrayList<Poll>()
    private lateinit var pollViewModel: PollViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.context = context

        /*val appDatabase = AppDatabase.getDatabase(context)
        val pollDao = appDatabase.pollDao()
        val repository = PollRepository(pollDao)
        val viewModelFactory = PollViewModelFactory(repository)
        pollViewModel = ViewModelProvider(this, viewModelFactory)[PollViewModel::class.java]*/
        pollViewModel = (requireActivity() as MainActivity).pollViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
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
                Log.e("HistoryFragment", "update poll check 3 observe: $poll")
                if (poll.isPollAnswered == 1) {
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

    override fun onDestroyView() {
        super.onDestroyView()
    }
}