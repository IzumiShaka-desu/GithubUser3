package com.darkshandev.githubuser.app.DI

import android.content.Context
import androidx.room.Room
import com.darkshandev.githubuser.app.Config
import com.darkshandev.githubuser.data.database.AppDatabase
import com.darkshandev.githubuser.data.datasources.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object LocalModule {
    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext appContext: Context
    ): AppDatabase = Room
        .databaseBuilder(
            appContext,
            AppDatabase::class.java,
            Config.DB_Name
        ).build()

    @Provides
    fun provideUserDao(appDatabase: AppDatabase): UserDao {
        return appDatabase.userDao()
    }
}