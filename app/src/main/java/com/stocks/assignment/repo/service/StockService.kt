package com.stocks.assignment.repo.service

import com.stocks.assignment.model.HoldingResponseModel
import retrofit2.http.GET

interface StockService {
    /**
     * Get the Holding response object.
     *
     */
    @GET("6d0ad460-f600-47a7-b973-4a779ebbaeaf")
    //@GET("e3c4d267-ae5a-47a6-9103-50382e79188e")
    suspend fun getHoldingList(): HoldingResponseModel
}
