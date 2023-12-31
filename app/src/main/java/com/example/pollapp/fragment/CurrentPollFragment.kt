package com.example.pollapp.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.pollapp.activity.MainActivity
import com.example.pollapp.adapter.CurrentPollAdapter
import com.example.pollapp.architecture.PollViewModel
import com.example.pollapp.databinding.FragmentCurrentPollBinding
import com.example.pollapp.data.Poll
import com.example.pollapp.utils.LinearLayoutManagerWrapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CurrentPollFragment : Fragment() {

    private lateinit var binding: FragmentCurrentPollBinding
    private lateinit var context: Context

    private val currentPollList = ArrayList<Poll>()
    private lateinit var pollViewModel: PollViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.context = context
        pollViewModel = (requireActivity() as MainActivity).pollViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentCurrentPollBinding.inflate(inflater, container, false)
        prepareRecyclerView()
        return binding.root
    }

    private fun prepareRecyclerView() {
        binding.rvCurrentPolls.layoutManager = LinearLayoutManagerWrapper(context, RecyclerView.VERTICAL, false)
        val adapter = CurrentPollAdapter(context, currentPollList, true, onOptionSelected = {
            currentPollList[it].anyOptionSelected = 1
            CoroutineScope(Dispatchers.IO).launch {
                pollViewModel.updatePoll(currentPollList[it])
            }
        })
        binding.rvCurrentPolls.adapter = adapter
        updateViewOnBlankList(currentPollList.isEmpty())

        pollViewModel.allPolls.observe(viewLifecycleOwner) { polls ->
            currentPollList.clear()
            for (poll in polls) {
                if (poll.isPollAnswered == 0) {
                    currentPollList.add(poll)
                }
            }
            adapter.submitPolls(currentPollList)
            updateViewOnBlankList(currentPollList.isEmpty())
        }
    }

    private fun updateViewOnBlankList(isListEmpty: Boolean) {
        if (isListEmpty) {
            binding.rvCurrentPolls.visibility = View.GONE
            binding.tvNoPolls.visibility = View.VISIBLE
        } else {
            binding.rvCurrentPolls.visibility = View.VISIBLE
            binding.tvNoPolls.visibility = View.GONE
        }
    }
}