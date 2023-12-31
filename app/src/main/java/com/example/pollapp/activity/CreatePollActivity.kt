package com.example.pollapp.activity

import android.content.Context
import android.os.Bundle
import android.os.Vibrator
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pollapp.adapter.CreatePollOptionsAdapter
import com.example.pollapp.architecture.PollRepository
import com.example.pollapp.architecture.PollViewModel
import com.example.pollapp.architecture.PollViewModelFactory
import com.example.pollapp.data.Option
import com.example.pollapp.databinding.ActivityCreatePollBinding
import com.example.pollapp.roomdbclasses.AppDatabase
import com.example.pollapp.data.Poll
import com.example.pollapp.utils.ItemTouchHelperCallback

class CreatePollActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreatePollBinding

    private val optionsList = ArrayList<String>()
    private lateinit var pollViewModel: PollViewModel
    private lateinit var itemTouchHelper: ItemTouchHelper
    private lateinit var optionsAdapter: CreatePollOptionsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatePollBinding.inflate(layoutInflater)

        val appDatabase = AppDatabase.getDatabase(this)
        val pollDao = appDatabase.pollDao()
        val repository = PollRepository(pollDao)
        val viewModelFactory = PollViewModelFactory(repository)
        pollViewModel = ViewModelProvider(this, viewModelFactory)[PollViewModel::class.java]

        setToolBar()
        prepareRecyclerView()
        btnListener()
        setContentView(binding.root)
    }

    private fun setToolBar() {
        setSupportActionBar(binding.toolBarCreatePoll)
        binding.toolBarCreatePoll.setNavigationOnClickListener {
            finish()
        }
    }

    private fun btnListener() {
        binding.etQuestion.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null) {
                    if (s.isEmpty()) {
                        binding.etQuestion.error = "Poll question can not be null"
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        binding.btnAddOption.setOnClickListener {
            if (optionsList.size < 5) {
                optionsList.add("")
                val x = "You can add ${5 - optionsList.size} more Options"
                binding.tvRemainingCount.text = x
                optionsAdapter.notifyItemInserted(optionsList.size - 1)
                if (optionsList.size == 5) {
                    binding.btnAddOption.isEnabled = false
                }
            } else {
                // Handle maximum option limit reached
            }
        }

        binding.btnCreatePoll.setOnClickListener {
            if (binding.etQuestion.text.isEmpty()) {
                binding.etQuestion.error = "Poll question can not be blank"
            } else if (optionsList.size < 2) {
                Toast.makeText(this, "Poll requires minimum of 2 options", Toast.LENGTH_SHORT).show()
            } else if (!optionsAdapter.checkEmptyViews()) {
                Toast.makeText(this, "Minimum 2 options are required", Toast.LENGTH_SHORT).show()
            } else {
                val question = binding.etQuestion.text.toString()
                val options = ArrayList<Option>()
                for (str in optionsList) {
                    if (str.isNotEmpty()) {
                        options.add(Option(0, str, 0))
                    }
                }
                val poll = Poll(0, question, options)
                pollViewModel.insert(poll)
                finish()
            }
        }
    }

    private fun prepareRecyclerView() {
        binding.rvOption.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        optionsAdapter = CreatePollOptionsAdapter(optionsList, onOptionRemoved = {
            optionsList.removeAt(it)
            val x = "You can add ${5 - optionsList.size} more Options"
            binding.tvRemainingCount.text = x
            optionsAdapter.notifyItemRemoved(it)
            if (!binding.btnAddOption.isEnabled) {
                binding.btnAddOption.isEnabled = true
            }
        }, onDragStart = {
            binding.rvOption.findViewHolderForAdapterPosition(it)?.let { it1 -> itemTouchHelper.startDrag(it1) }
        }, onOptionNext = {
            binding.btnAddOption.performClick()
        }, onOptionDone = {
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(
                binding.root.windowToken, 0
            )
        })

        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        val itemTouchHelperCallback = ItemTouchHelperCallback(optionsAdapter, vibrator)
        itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.rvOption)

        binding.rvOption.adapter = optionsAdapter
    }
}