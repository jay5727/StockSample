package com.stocks.assignment.utils

import java.math.RoundingMode
import java.text.DecimalFormat

/**
 * @return a zero value for any Integer which is null, else return the original value.
 */
fun Int?.orZero(): Int = this ?: 0

fun Long?.orZero(): Long = this ?: 0L

fun Double?.orZero(): Double = this ?: 0.0

fun Double?.getRoundUpto2(): Double {

    val df = DecimalFormat("#.00")
    df.roundingMode = RoundingMode.DOWN
    val roundoff = df.format(this)
    return roundoff.toDouble()
}