package com.crypto.demo

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ICurrencyInfoDao {
    @Query("Select * from currency")
    fun getAll(): Flow<List<CurrencyInfo>>
}