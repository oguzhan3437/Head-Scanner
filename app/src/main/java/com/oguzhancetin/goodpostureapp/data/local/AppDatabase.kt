package com.oguzhancetin.goodpostureapp.data.local

import android.app.Application
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.oguzhancetin.goodpostureapp.data.model.Record

@Database(entities = [Record::class], version = 1)
abstract class AppDatabase : RoomDatabase() {


    companion object {
        @Volatile
        private var database: AppDatabase? = null
        fun getNewInstance(context: Application): AppDatabase {
            if (database == null) {
                return Room.databaseBuilder(context, AppDatabase::class.java, "ForwardHeadDb")
                    .build()
            }
            return database!!
        }
    }


    abstract fun userDao(): RecordsDao
}