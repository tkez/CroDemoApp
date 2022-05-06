package com.crypto.demo

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CurrencyInfo::class], version = 1, exportSchema = false)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun currencyInfoDao(): ICurrencyInfoDao

    companion object {
        private var INSTANCE: LocalDatabase? = null

        fun getDatabase(context: Context): LocalDatabase {
            return INSTANCE ?: synchronized(this) {
                val db = Room
                    .databaseBuilder(
                        context,
                        LocalDatabase::class.java,
                        "local_database"
                    )
                    .createFromAsset("local.db")
                    .build()
                INSTANCE = db

                db
            }
        }
    }
}