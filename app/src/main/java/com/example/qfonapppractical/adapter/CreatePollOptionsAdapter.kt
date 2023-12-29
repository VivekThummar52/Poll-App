package com.example.vivekpracticalnov1.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.RecyclerView
import com.example.vivekpracticalnov1.databinding.ItemCreatePollOptionLayoutBinding
import com.example.vivekpracticalnov1.interfaces.ItemTouchHelperAdapter
import java.util.Collections

class CreatePollOptionsAdapter(
    private val options: ArrayList<String>,
    private val onOptionRemoved: (position: Int) -> Unit,
    private val onDragStart: (position: Int) -> Unit,
    private val onOptionNext: (position: Int) -> Unit,
    private val onOptionDone: () -> Unit
) : RecyclerView.Adapter<CreatePollOptionsAdapter.ViewHolder>(), ItemTouchHelperAdapter {

    class ViewHolder(val binding: ItemCreatePollOptionLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCreatePollOptionLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val adapterPosition = holder.adapterPosition
        holder.binding.optionEditText.setText(options[adapterPosition])

        if (adapterPosition < 4) {
            holder.binding.optionEditText.imeOptions = EditorInfo.IME_ACTION_NEXT
        } else {
            holder.binding.optionEditText.imeOptions = EditorInfo.IME_ACTION_DONE
        }

        holder.binding.optionEditText.setOnEditorActionListener { _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_NEXT -> {
                    onOptionNext(adapterPosition)
                    true
                }
                EditorInfo.IME_ACTION_DONE -> {
                    onOptionDone()
                    true
                }
                else -> false
            }
        }

        holder.binding.dragHandle.setOnClickListener {
            onDragStart(adapterPosition)
        }

        holder.binding.removeOption.setOnClickListener {
            onOptionRemoved(adapterPosition)
        }

        holder.binding.optionEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                options[adapterPosition] = s.toString()
                val newOption = s.toString()
                val existingOptions = options.toMutableList()
                existingOptions.removeAt(adapterPosition)
                if (existingOptions.contains(newOption)) {
                    holder.binding.optionEditText.error = "Option already exists"
                } else {
                    // Clear the error when the input is valid
                    holder.binding.optionEditText.error = null
                    options[adapterPosition] = newOption
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    override fun getItemCount(): Int {
        return options.size
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        Collections.swap(options, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
        return true
    }

    override fun onItemDismiss(position: Int) {
        options.removeAt(position)
        onOptionRemoved(position)
        notifyItemRemoved(position)
    }

    fun checkEmptyViews(): Boolean {
        var count = 0
        for (i in options.indices) {
            if (options[i].isNotEmpty()) {
                count++
            }
        }
        return count >= 2
    }
}