package com.stocks.assignment.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stocks.assignment.model.Holding
import com.stocks.assignment.repo.StockRepository
import com.stocks.assignment.utils.Resource
import com.stocks.assignment.utils.StringConstants
import com.stocks.assignment.utils.StringConstants.CurrentValue
import com.stocks.assignment.utils.StringConstants.INRSymbol
import com.stocks.assignment.utils.StringConstants.PNL
import com.stocks.assignment.utils.StringConstants.TodaysPL
import com.stocks.assignment.utils.StringConstants.TotalInvestment
import com.stocks.assignment.utils.getRoundUpto2
import com.stocks.assignment.utils.orZero
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Jay on 01-Oct-2022
 */
@HiltViewModel
class HoldingViewModel @Inject constructor(
    private val repository: StockRepository
) : ViewModel() {

    val pairList = arrayListOf<Pair<String, String>>()

    private val holdingList = MutableLiveData<Resource<List<Holding>>>()
    fun getHoldingList(): LiveData<Resource<List<Holding>>> {
        return holdingList
    }

    init {
        fetchHoldingList()
    }

    val loader: LiveData<Boolean> = MutableLiveData(false)
    fun showLoader(toShow: Boolean) {
        (loader as MutableLiveData).value = toShow
    }

    private fun fetchHoldingList() {
        viewModelScope.launch {
            holdingList.postValue(Resource.loading(null))
            repository.getHoldingList()
                .catch { e ->
                    holdingList.postValue(Resource.error(e.toString(), null))
                }
                .collect { response ->
                    response.data.map {
                        it.formattedLTP = getFormattedTextWithRupees(
                            value = it.ltp,
                            label = StringConstants.LTP
                        )
                        val pnl = getPNL(
                            ltp = it.ltp,
                            quantity = it.quantity,
                            avgPrice = it.avgPrice
                        )
                        it.formattedPNL = getFormattedTextWithRupees(
                            value = pnl,
                            label = StringConstants.PL
                        )

                    }
                    response.apply {
                        //7
                        val currentValue = response.data.sumOf {
                            getCurrentValue(
                                ltp = it.ltp,
                                quantity = it.quantity
                            )
                        }
                        //8
                        val totalInvestment = response.data.sumOf {
                            getInvestmentValue(
                                avgPrice = it.avgPrice,
                                quantity = it.quantity
                            )
                        }
                        //9
                        val totalPNL = currentValue.orZero() - totalInvestment.orZero()
                        //10
                        val todaysPNL = getDayPnL(response.data)

                        setPnLBottomInfo(
                            currentValue = currentValue,
                            totalInvestment = totalInvestment,
                            totalPNL = totalPNL,
                            todaysPNL = todaysPNL
                        )
                    }
                    holdingList.postValue(Resource.success(response.data))
                }
        }
    }

    private fun setPnLBottomInfo(
        currentValue: Double?,
        totalInvestment: Double?,
        totalPNL: Double?,
        todaysPNL: Double?,
    ) {
        addToPairs(CurrentValue, currentValue.toString())
        addToPairs(TotalInvestment, totalInvestment.toString())
        addToPairs(TodaysPL, todaysPNL.toString())
        addToPairs(PNL, totalPNL.toString())

    }

    private fun addToPairs(label: String, amount: String) {
        pairList.add(
            Pair(
                first = label,
                second = "$INRSymbol$amount"
            )
        )
    }

    private fun getDayPnL(list: List<Holding>): Double {
        return list.sumOf { (it.close.orZero() - it.ltp.orZero()) * it.quantity.orZero() }
    }

    private fun getFormattedTextWithRupees(value: Double?, label: String): String {
        return if (value != null) {
            StringBuilder()
                .append(label)
                .append(StringConstants.COLON)
                .append(StringConstants.SPACE)
                .append(INRSymbol).append(value).toString()
        } else {
            ""
        }
    }

    /**
     * Function to get the data of every individual item
     */
    private fun getPNL(
        ltp: Double? = null,
        quantity: Int? = null,
        avgPrice: String? = null
    ): Double {
        //5
        val currentValue: Double = getCurrentValue(ltp = ltp, quantity = quantity)
        //6
        val investmentValue: Double = getInvestmentValue(avgPrice = avgPrice, quantity = quantity)
        //4
        return (currentValue - investmentValue).getRoundUpto2()
    }

    private fun getCurrentValue(
        ltp: Double? = null,
        quantity: Int? = null
    ) = ltp.orZero() * quantity.orZero()

    private fun getInvestmentValue(
        quantity: Int? = null,
        avgPrice: String? = null
    ) = avgPrice?.toDoubleOrNull().orZero() * quantity.orZero()
}