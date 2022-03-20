package com.darkshandev.githubuser.data.datasources

import androidx.room.*
import com.darkshandev.githubuser.data.models.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM user_table")
    fun getFavUser(): Flow<List<UserEntity>>

    @Delete
    suspend fun delete(user: UserEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavoriteUser(user: UserEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAll(users: List<UserEntity>)

    @Delete
    suspend fun deleteAll(users: List<UserEntity>)

}