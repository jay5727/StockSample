package com.stocks.assignment.view

import android.os.Bundle
import android.view.View
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
        setupBinding()
        setupObserver()
    }

    override fun onRetryClicked() {
        viewModel.fetchHoldingList()
    }

    private fun setupBinding() {
        binding.callback = this
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }

    private fun setupObserver() {
        viewModel.screenState.observe(this) {
            when (it) {
                is HoldingViewModel.ScreenState.ListSuccessState -> {
                    binding.run {

                        if (!it.holdingList.isNullOrEmpty()) {

                            with(recyclerViewHolding) {
                                adapter = HoldingAdapter(
                                    holdingList = it.holdingList
                                )
                                //We don't need seperator for last item
                                addItemDecorationWithoutLastDivider()
                                setHasFixedSize(true)
                            }

                            //P&L Info Bottom Info
                            PnlBottomInfoAdapter(
                                pairList = binding.viewModel?.pairList ?: emptyList()
                            ).also { adapter ->
                                recyclerViewPnlInfo.apply {
                                    this.adapter = adapter
                                    visibility = View.VISIBLE
                                    setHasFixedSize(true)
                                }
                            }

                        }
                    }
                }
                else -> {
                    // no - op
                }
            }
        }
    }

}