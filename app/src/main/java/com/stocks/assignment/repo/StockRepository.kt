package com.stocks.assignment.repo

import com.stocks.assignment.model.HoldingResponseModel
import com.stocks.assignment.repo.service.StockService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockRepository @Inject constructor(
    private val stockService: StockService
) {

    fun getHoldingResponse(): Flow<HoldingResponseModel> = flow {
        emit(stockService.getHoldingList())
    }
}