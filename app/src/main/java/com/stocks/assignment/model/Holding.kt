package com.stocks.assignment.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Jay on 01-Oct-2022
 */
data class Holding(

    @SerializedName("avg_price")
    var avgPrice: String? = null,

    @SerializedName("cnc_used_quantity")
    var cncUsedQuantity: Int? = null,

    @SerializedName("collateral_qty")
    var collateralQty: Int? = null,

    @SerializedName("collateral_type")
    var collateralType: String? = null,

    @SerializedName("collateral_update_qty")
    var collateralUpdateQty: Int? = null,

    @SerializedName("company_name")
    var companyName: String? = null,

    @SerializedName("haircut")
    var haircut: Double? = null,

    @SerializedName("holdings_update_qty")
    var holdingsUpdateQty: Int? = null,

    @SerializedName("isin")
    var isin: String? = null,

    @SerializedName("product")
    var product: String? = null,

    @SerializedName("quantity")
    var quantity: Int? = null,

    @SerializedName("symbol")
    var symbol: String? = null,

    @SerializedName("t1_holding_qty")
    var t1HoldingQty: Int? = null,

    @SerializedName("token_bse")
    var tokenBse: String? = null,

    @SerializedName("token_nse")
    var tokenNse: String? = null,

    @SerializedName("withheld_collateral_qty")
    var withheldCollateralQty: Int? = null,

    @SerializedName("withheld_holding_qty")
    var withheldHoldingQty: Int? = null,

    @SerializedName("ltp")
    var ltp: Double? = null,

    @SerializedName("close")
    var close: Int? = null
)
