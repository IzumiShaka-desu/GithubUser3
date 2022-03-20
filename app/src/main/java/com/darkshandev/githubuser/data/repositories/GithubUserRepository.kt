package com.darkshandev.githubuser.data.repositories

import com.darkshandev.githubuser.data.datasources.RemoteDataSource
import com.darkshandev.githubuser.data.models.Result
import com.darkshandev.githubuser.data.models.SearchResponse
import com.darkshandev.githubuser.data.models.UserDetail
import com.darkshandev.githubuser.data.models.UserSearch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class GithubUserRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) {

    private val _query = MutableStateFlow("")
    fun setQuery(query: String) {
        _query.value = query
    }

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
}