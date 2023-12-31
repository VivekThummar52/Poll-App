package com.example.pollapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pollapp.data.Option
import com.example.pollapp.databinding.ItemOptionViewBinding

class NestedOptionAdapter(
    private val options: List<Option>,
    private val anyOptionSelected: Int,
    private val shouldEdit: Boolean,
    private val onOptionSelected: () -> Unit
) : RecyclerView.Adapter<NestedOptionAdapter.ViewHolder>() {

    private var checkedPosition = -1

    class ViewHolder(val binding: ItemOptionViewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemOptionViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val adapterPosition = holder.adapterPosition
        val option = options[adapterPosition]

        holder.binding.tvOption.text = option.text
        holder.binding.optionProgress.setProgressCompat(option.progress, true)

        if (option.progress == 100) {
            holder.binding.ivOptionNotSelected.visibility = View.GONE
            holder.binding.ivOptionSelected.visibility = View.VISIBLE
            holder.binding.tvPercentage.text = "100%"
        } else {
            holder.binding.ivOptionSelected.visibility = View.GONE
            holder.binding.tvPercentage.text = "0%"
            if (anyOptionSelected == 0) {
                holder.binding.tvPercentage.visibility = View.GONE
                holder.binding.ivOptionNotSelected.visibility = View.VISIBLE
            } else {
                holder.binding.tvPercentage.visibility = View.VISIBLE
                holder.binding.ivOptionNotSelected.visibility = View.GONE
            }
        }

        holder.binding.llOptionRoot.setOnClickListener {
            if (shouldEdit) {
                onOptionSelected()
                if (checkedPosition != adapterPosition) {
                    option.progress = 100
                    if (checkedPosition != -1) {
                        options[checkedPosition].progress = 0
                        notifyItemChanged(checkedPosition)
                    }
                    checkedPosition = adapterPosition
                    notifyItemChanged(checkedPosition)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return options.size
    }
}