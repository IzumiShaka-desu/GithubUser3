package com.darkshandev.githubuser.data.datasources

import android.util.Log
import com.darkshandev.githubuser.data.models.Result
import com.darkshandev.githubuser.data.models.SearchResponse
import com.darkshandev.githubuser.data.models.UserDetail
import com.darkshandev.githubuser.data.models.UserSearch
import com.darkshandev.githubuser.data.network.UserService
import com.darkshandev.githubuser.utils.ErrorUtils
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val retrofit: Retrofit) {
    private val service = retrofit.create(UserService::class.java)
    suspend fun searchUserBy(username: String): Result<SearchResponse> =
        getResponse(defaultErrorMessage = "Error fetching Movie list") {
            service.getSearchUser(username)
        }

    suspend fun getDetailUserBy(username: String): Result<UserDetail> =
        getResponse(defaultErrorMessage = "Error fetching Detail user") {
            service.getDetailUser(username)
        }

    suspend fun getFollowerUserBy(username: String): Result<List<UserSearch>> =
        getResponse(defaultErrorMessage = "Error fetching follower") {
            service.getUserFollowers(username)
        }

    suspend fun getFollowingUserBy(username: String): Result<List<UserSearch>> =
        getResponse(defaultErrorMessage = "Error fetching following") {
            service.getUserFollowing(username)
        }

    private suspend fun <T> getResponse(
        defaultErrorMessage: String,
        request: suspend () -> Response<T>

    ): Result<T> {

        return try {

            val result = request.invoke()
            if (result.isSuccessful) {
                return Result.success(result.body())
            } else {
                val errorResponse = ErrorUtils.parseError(result, retrofit)
                Result.error(errorResponse?.localizedMessage ?: defaultErrorMessage, errorResponse)
            }
        } catch (e: Throwable) {
            Log.e("get result : ", e.localizedMessage)
            Result.error("Unable to complete your request, please try again", null)
        }
    }
}