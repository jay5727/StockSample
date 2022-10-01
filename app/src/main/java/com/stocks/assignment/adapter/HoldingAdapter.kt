package com.stocks.assignment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.stocks.assignment.databinding.ListItemHoldingBinding
import com.stocks.assignment.model.Holding

/**
 * Created by Jay on 01-Oct-2022
 */
class HoldingAdapter(
    private val holdingList: List<Holding>
) : RecyclerView.Adapter<HoldingViewHolder>() {

    private lateinit var binding: ListItemHoldingBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HoldingViewHolder {
        binding = ListItemHoldingBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HoldingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HoldingViewHolder, position: Int) {
        holder.bind(holdingList[position])
    }

    override fun getItemCount(): Int = holdingList.size

}

class HoldingViewHolder(
    private val binding: ListItemHoldingBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(holding: Holding) {
        binding.holding = holding
    }
}