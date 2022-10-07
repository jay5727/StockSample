package com.stocks.assignment.utils

import com.stocks.assignment.model.Holding

/**
 * @return formatted string
 * eg
 * LTP: ₹5727
 * P&L: ₹5727
 */
fun Double?.getFormattedTextWithRupees(label: String): String {
    return if (this != null) {
        StringBuilder()
            .append(label)
            .append(StringConstants.COLON)
            .append(StringConstants.SPACE)
            .append(StringConstants.INRSymbol)
            .append(StringConstants.SPACE)
            .append(this)
            .toString()
    } else {
        ""
    }
}

/**
 * Function to get the data of every individual item
 */
fun Holding?.getPNL(): Double {
    //5
    val currentValue: Double = this?.getCurrentValue().orZero()
    //6
    val investmentValue: Double = this?.getInvestmentValue().orZero()
    //4
    return (currentValue - investmentValue).getRoundUpto2Decimals()
}

fun Holding?.getCurrentValue(
): Double = this?.ltp.orZero() * this?.quantity.orZero()

/**
 * Correction: 6 It is AvgPrice * Qty not AvgPrice - Qty
 */
fun Holding?.getInvestmentValue(
): Double = this?.avgPrice?.toDoubleOrNull().orZero() * this?.quantity.orZero()

fun getDayPnL(list: List<Holding>): Double {
    return list.sumOf { (it.close.orZero() - it.ltp.orZero()) * it.quantity.orZero() }
}

