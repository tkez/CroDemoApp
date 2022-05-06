package com.crypto.demo

import androidx.annotation.DrawableRes

data class MainActivityUIState(
    val currencyInfoList: List<CurrencyInfo>,
    val sortAscending: Boolean?,
    @DrawableRes val sortDrawableId: Int?
)