package com.stocks.assignment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.stocks.assignment.R
import com.stocks.assignment.databinding.ListPnlBottomInfoBinding

/**
 * Created by Jay on 02-Oct-2022
 */
class PnlBottomInfoAdapter(
    private val pairList: List<Pair<String, String>>
) : RecyclerView.Adapter<PnlBottomInfoViewHolder>() {

    private lateinit var binding: ListPnlBottomInfoBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PnlBottomInfoViewHolder {
        binding = ListPnlBottomInfoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PnlBottomInfoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PnlBottomInfoViewHolder, position: Int) {
        holder.bind(
            pair = pairList[position],
            isLastItem = position == pairList.lastIndex
        )
    }

    override fun getItemCount(): Int = pairList.size
}

class PnlBottomInfoViewHolder(
    private val binding: ListPnlBottomInfoBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(pair: Pair<String, String>, isLastItem: Boolean = false) {
        binding.data = pair
        if (isLastItem) {
            binding.topPaddingPixels =
                binding.root.context.resources.getDimension(R.dimen.dimen_16dp).toInt()
        }
    }
}