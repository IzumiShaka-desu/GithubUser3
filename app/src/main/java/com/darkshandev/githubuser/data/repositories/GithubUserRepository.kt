package com.darkshandev.githubuser.data.repositories

import androidx.annotation.WorkerThread
import com.darkshandev.githubuser.data.datasources.RemoteDataSource
import com.darkshandev.githubuser.data.datasources.UserDao
import com.darkshandev.githubuser.data.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GithubUserRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val userDao: UserDao
) {

    private val _query = MutableStateFlow("")
    fun setQuery(query: String) {
        _query.value = query
    }

    @ExperimentalCoroutinesApi
    @FlowPreview
    fun searchUser(): Flow<Result<SearchResponse>> = _query.debounce(300)
        .flatMapLatest { query ->
            flow {
                if (query.isEmpty()) {
                    emit(
                        Result.error(
                            message = "type something a discover new github friends",
                            Error()
                        )
                    )
                } else {
                    emit(remoteDataSource.searchUserBy(query))
                }
            }.onStart {
                emit(Result.loading())
            }.catch {
                emit(
                    Result.error(
                        message = "Sorry, there are no pups by that name. Keep looking!",
                        Error()
                    )
                )
            }
        }
        .flowOn(Dispatchers.IO)

    suspend fun getDetailUserBy(username: String): Flow<Result<UserDetail>> = flow {
        emit(Result.loading())
        val result = remoteDataSource.getDetailUserBy(username)
        emit(result)
    }.flowOn(Dispatchers.IO)

    suspend fun getFollowingUserBy(username: String): Flow<Result<List<UserSearch>>> = flow {
        emit(Result.loading())
        val result = remoteDataSource.getFollowingUserBy(username)
        emit(result)
    }.flowOn(Dispatchers.IO)

    suspend fun getFollowerUserBy(username: String): Flow<Result<List<UserSearch>>> = flow {
        emit(Result.loading())
        val result = remoteDataSource.getFollowerUserBy(username)
        emit(result)
    }.flowOn(Dispatchers.IO)

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun addFavorite(user: UserEntity) {
        withContext(Dispatchers.IO) {
            userDao.addFavoriteUser(user)
        }
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun removeFavorite(user: UserEntity) {
        withContext(Dispatchers.IO) {
            userDao.delete(user)
        }
    }

    fun getFavoriteUser() = userDao.getFavUser()
}