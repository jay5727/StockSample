package com.stocks.assignment.utils

import android.view.View
import androidx.databinding.BindingAdapter


object ViewBindingAdapters {

    /**
     * Binding adapter to set all margins for a view, in [Int]
     */
    @JvmStatic
    @BindingAdapter(
        value = ["paddingLeftPixels", "paddingRightPixels", "paddingTopPixels", "paddingBottomPixels"],
        requireAll = false
    )
    fun setViewPaddingInPixels(
        view: View,
        paddingLeft: Int?,
        paddingRight: Int?,
        paddingTop: Int?,
        paddingBottom: Int?
    ) {
        view.setPadding(
            paddingLeft ?: view.paddingLeft,
            paddingTop ?: view.paddingTop,
            paddingRight ?: view.paddingRight,
            paddingBottom ?: view.paddingBottom
        )
    }
}