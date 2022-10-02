package com.stocks.assignment.viewmodel

import androidx.lifecycle.*
import com.stocks.assignment.model.Holding
import com.stocks.assignment.model.HoldingResponseModel
import com.stocks.assignment.repo.StockRepository
import com.stocks.assignment.utils.*
import com.stocks.assignment.utils.StringConstants.CurrentValue
import com.stocks.assignment.utils.StringConstants.INRSymbol
import com.stocks.assignment.utils.StringConstants.PNL
import com.stocks.assignment.utils.StringConstants.TodaysPL
import com.stocks.assignment.utils.StringConstants.TotalInvestment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.net.*
import javax.inject.Inject

/**
 * Created by Jay on 01-Oct-2022
 */
@HiltViewModel
class HoldingViewModel @Inject constructor(
    private val repository: StockRepository
) : ViewModel() {

    companion object {
        private val NoData = "No Data Found"
        private val NoInternet = "Sorry! Connection failed"
        private val GenericError = "Something went wrong."
    }
    val screenState = MutableLiveData<ScreenState>()

    val holdingList: LiveData<List<Holding>?> = screenState.map {
        (it as? ScreenState.ListSuccessState)?.holdingList
    }

    val holdingListVisibility: LiveData<Boolean> = holdingList.map {
        !it.isNullOrEmpty()
    }

    val errorMessage: LiveData<String?> = screenState.map {
        (it as? ScreenState.ErrorState)?.message
    }

    val errorViewVisible: LiveData<Boolean> = screenState.map {
        (it is ScreenState.ErrorState)
    }

    val loaderVisibility: LiveData<Boolean> = screenState.map {
        (it is ScreenState.Loading)
    }

    val pairList = arrayListOf<Pair<String, String>>()

    init {
        fetchHoldingList()
    }

    fun fetchHoldingList() {
        viewModelScope.launch {
            screenState.value = ScreenState.Loading
            repository.getHoldingList()
                .catch { error ->
                    val message = if (isNoNetworkError(error)) {
                        NoInternet
                    } else {
                        GenericError
                    }
                    screenState.value = ScreenState.ErrorState(
                        message = message
                    )
                }
                .collect { response ->
                    parseHoldingResponse(response = response)
                }
        }
    }

    private fun parseHoldingResponse(response: HoldingResponseModel) {
        if (response.data.isNullOrEmpty()) {
            screenState.postValue(
                ScreenState.ErrorState(
                    message = NoData
                )
            )
            return
        }
        response.data.map {
            it.formattedLTP = it.ltp.getFormattedTextWithRupees(label = StringConstants.LTP)
            it.formattedPNL = it.getPNL().getFormattedTextWithRupees(label = StringConstants.PL)
        }
        response.apply {

            //7
            val currentValue = response.data.sumOf { it.getCurrentValue() }

            //8
            val totalInvestment = response.data.sumOf { it.getInvestmentValue() }

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

            screenState.postValue(
                ScreenState.ListSuccessState(
                    holdingList = response.data
                )
            )
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

    private fun isNoNetworkError(error: Throwable?): Boolean {
        error?.let {
            return when (it) {
                is SocketException,
                is UnknownHostException,
                is BindException,
                is ConnectException,
                is NoRouteToHostException -> true

                else -> false
            }
        }

        return false
    }

    sealed class ScreenState {
        object Loading : ScreenState()

        data class ListSuccessState(
            val holdingList: List<Holding>?
        ) : ScreenState()

        data class ErrorState(
            val message: String?
        ) : ScreenState()
    }
}