package com.example.qfonapppractical.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.qfonapppractical.activity.MainActivity
import com.example.qfonapppractical.adapter.CurrentPollAdapter
import com.example.qfonapppractical.architecture.PollViewModel
import com.example.qfonapppractical.databinding.FragmentCurrentPollBinding
import com.example.qfonapppractical.roomdbclasses.Poll
import com.example.qfonapppractical.utils.LinearLayoutManagerWrapper
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
        binding = FragmentCurrentPollBinding.inflate(inflater, container, false)
        prepareRecyclerView()
        return binding.root
    }

    private fun prepareRecyclerView() {
        binding.rvCurrentPolls.layoutManager = LinearLayoutManagerWrapper(context, RecyclerView.VERTICAL, false)
        val adapter = CurrentPollAdapter(context, currentPollList, true, onOptionSelected = {
            currentPollList[it].anyOptionSelected = 1
            CoroutineScope(Dispatchers.IO).launch {
                val x = pollViewModel.updatePoll(currentPollList[it])
                Log.e("CurrentPollFragment", "update poll check 2 onOptionSelected: $x, poll id = ${currentPollList[it].id}")
            }
        })
        binding.rvCurrentPolls.adapter = adapter
        updateViewOnBlankList(currentPollList.isEmpty())

        pollViewModel.allPolls.observe(viewLifecycleOwner) { polls ->
            currentPollList.clear()
            for (poll in polls) {
                Log.e("CurrentPollFragment", "update poll check 2 observe: $poll")
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

    override fun onDestroyView() {
        super.onDestroyView()
    }
}