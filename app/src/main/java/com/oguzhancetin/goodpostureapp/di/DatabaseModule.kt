package com.oguzhancetin.goodpostureapp.di

import android.content.Context
import androidx.room.Room
import com.oguzhancetin.goodpostureapp.data.local.AppDatabase
import com.oguzhancetin.goodpostureapp.data.local.RecordsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Provides
    fun provideRecordDao(appDatabase: AppDatabase): RecordsDao {
        return appDatabase.recordDao()
    }

    @Provides
    @Singleton
    fun appDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "posture_database"
        ).build()

    }
}