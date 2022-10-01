package com.stocks.assignment.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.stocks.assignment.adapter.HoldingAdapter
import com.stocks.assignment.adapter.PnlBottomInfoAdapter
import com.stocks.assignment.base.BaseActivity
import com.stocks.assignment.databinding.ActivityMainBinding
import com.stocks.assignment.utils.Status
import com.stocks.assignment.viewmodel.HoldingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding

    // Get a reference to the ViewModel scoped to this Fragment
    private val viewModel by viewModels<HoldingViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        binding.viewModel = viewModel
        setupObserver()
    }

    private fun setupObserver() {
        viewModel.getHoldingList().observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    viewModel.showLoader(false)

                    binding.run {
                        recyclerViewHolding.apply {
                            addItemDecoration(
                                DividerItemDecoration(
                                    this.context,
                                    (this.layoutManager as LinearLayoutManager).orientation
                                )
                            )
                            if (!it.data.isNullOrEmpty()) {
                                adapter = HoldingAdapter(it.data)
                                setHasFixedSize(true)
                            } else {
                                //no data UI
                            }

                        }
                        recyclerViewPnlInfo.apply {
                            if (!it.data.isNullOrEmpty()) {
                                adapter = PnlBottomInfoAdapter(
                                    binding.viewModel?.pairList ?: emptyList()
                                )
                                setHasFixedSize(true)
                            } else {
                                //no data UI
                            }
                        }
                    }
                }
                Status.LOADING -> {
                    viewModel.showLoader(true)
                }
                Status.ERROR -> {
                    viewModel.showLoader(false)
                    //Handle Error
                }
            }
        }
    }
}