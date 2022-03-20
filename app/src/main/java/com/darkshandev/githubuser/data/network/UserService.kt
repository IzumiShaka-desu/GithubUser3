package com.darkshandev.githubuser.data.network

import com.darkshandev.githubuser.data.models.SearchResponse
import com.darkshandev.githubuser.data.models.UserDetail
import com.darkshandev.githubuser.data.models.UserSearch
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UserService {
    @GET("/search/users")
    suspend fun getSearchUser(
        @Query("q") keyword: String
    ): Response<SearchResponse>

    @GET("users/{username}")
    suspend fun getDetailUser(
        @Path("username") username: String
    ): Response<UserDetail>

    @GET("users/{username}")
    suspend fun getUser(
        @Path("username") username: String
    ): Response<UserDetail>

    @GET("users/{username}/followers")
    suspend fun getUserFollowers(
        @Path("username") username: String
    ): Response<List<UserSearch>>

    @GET("users/{username}/following")
    suspend fun getUserFollowing(
        @Path("username") username: String
    ): Response<List<UserSearch>>
}