package com.stocks.assignment.view

import android.os.Bundle
import androidx.activity.viewModels
import com.stocks.assignment.adapter.HoldingAdapter
import com.stocks.assignment.adapter.PnlBottomInfoAdapter
import com.stocks.assignment.base.BaseActivity
import com.stocks.assignment.callback.ErrorCallback
import com.stocks.assignment.databinding.ActivityMainBinding
import com.stocks.assignment.utils.addItemDecorationWithoutLastDivider
import com.stocks.assignment.viewmodel.HoldingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity(), ErrorCallback {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: HoldingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.callback= this
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        setupObserver()
    }

    override fun onRetryClicked() {
        viewModel.fetchHoldingList()
    }

    private fun setupObserver() {
        viewModel.screenState.observe(this) {
            when (it) {
                is HoldingViewModel.ScreenState.ListSuccessState -> {
                    binding.run {

                        if (!it.holdingList.isNullOrEmpty()) {
                            recyclerViewHolding.adapter = HoldingAdapter(
                                holdingList =  it.holdingList
                            )
                            //We don't need seperator for last item
                            recyclerViewHolding.addItemDecorationWithoutLastDivider()
                            recyclerViewHolding.setHasFixedSize(true)

                            //P&L Info Bottom Info
                            recyclerViewPnlInfo.adapter = PnlBottomInfoAdapter(
                                pairList = binding.viewModel?.pairList ?: emptyList()
                            )
                            recyclerViewPnlInfo.setHasFixedSize(true)
                        }
                    }
                }
            }
        }
    }

}