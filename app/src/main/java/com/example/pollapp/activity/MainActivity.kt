package com.example.pollapp.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.pollapp.R
import com.example.pollapp.architecture.PollRepository
import com.example.pollapp.architecture.PollViewModel
import com.example.pollapp.architecture.PollViewModelFactory
import com.example.pollapp.databinding.ActivityMainBinding
import com.example.pollapp.fragment.CurrentPollFragment
import com.example.pollapp.fragment.HistoryFragment
import com.example.pollapp.roomdbclasses.AppDatabase
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), LifecycleObserver {

    private lateinit var binding: ActivityMainBinding

    lateinit var pollViewModel: PollViewModel
    /*val pollViewModel: PollViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[PollViewModel::class.java]
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val appDatabase = AppDatabase.getDatabase(this)
        val pollDao = appDatabase.pollDao()
        val repository = PollRepository(pollDao)
        val viewModelFactory = PollViewModelFactory(repository)
        pollViewModel = ViewModelProvider(this, viewModelFactory)[PollViewModel::class.java]

        lifecycle.addObserver(this)

        val firstFragment = CurrentPollFragment()
        val secondFragment = HistoryFragment()
        setCurrentFragment(firstFragment)

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_current_poll -> setCurrentFragment(firstFragment)
                R.id.menu_history -> {
                    movePollToHistory()
                    setCurrentFragment(secondFragment)
                }
            }
            true
        }

        binding.fabCreatePoll.setOnClickListener {
            movePollToHistory()
            startActivity(Intent(this, CreatePollActivity::class.java))
        }
    }

    private fun movePollToHistory() {
        pollViewModel.allPolls.value?.forEach {
            if (it.anyOptionSelected == 1 && it.isPollAnswered == 0) {
                it.isPollAnswered = 1
                lifecycleScope.launch {
                    val x = pollViewModel.updatePoll(it)
                    Log.e("MainActivity", "update poll check 1 movePollToHistory(DB Record): $x")
                }
                Log.e("MainActivity", "update poll check 1 movePollToHistory: $it")
            }
        }

        /*pollViewModel.allPolls.observe(this) { polls ->
            for (poll in polls) {
                if (poll.anyOptionSelected == 1 && poll.isPollAnswered == 0) {
                    poll.isPollAnswered = 1
                    lifecycleScope.launch {
                        val x = pollViewModel.updatePoll(poll)
                        Log.e("MainActivity", "update poll check 1 movePollToHistory: $x")
                    }
                }
            }
        }*/
    }

    private fun setCurrentFragment(fragment: Fragment) = supportFragmentManager.beginTransaction().apply {
        replace(R.id.flFragment, fragment)
        commit()
    }
}