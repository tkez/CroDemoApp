package com.crypto.demo

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currency")
data class CurrencyInfo(
    @PrimaryKey
    val id: String,
    @NonNull
    val name: String,
    @NonNull
    val symbol: String
)