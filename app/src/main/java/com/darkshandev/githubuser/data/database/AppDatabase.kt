package com.darkshandev.githubuser.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.darkshandev.githubuser.data.datasources.UserDao
import com.darkshandev.githubuser.data.models.UserEntity


@Database(
    entities = [UserEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}