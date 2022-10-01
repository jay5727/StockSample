package com.stocks.assignment.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Jay on 01-Oct-2022
 */
data class HoldingResponseModel(

    @SerializedName("client_id")
    val clientId: String? = null,

    @SerializedName("data")
    val data: ArrayList<Holding> = arrayListOf(),

    @SerializedName("error")
    val error: String? = null,

    @SerializedName("response_type")
    val responseType: String? = null,

    @SerializedName("timestamp")
    val timestamp: Long? = null

)