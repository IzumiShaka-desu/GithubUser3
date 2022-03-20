package com.darkshandev.githubuser.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.darkshandev.githubuser.data.models.Result
import com.darkshandev.githubuser.data.models.SearchResponse
import com.darkshandev.githubuser.data.models.UserDetail
import com.darkshandev.githubuser.data.models.UserSearch
import com.darkshandev.githubuser.data.repositories.GithubUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class MainViewmodel @Inject constructor(
    private val repository: GithubUserRepository
) : ViewModel() {
    fun setQuery(query: String) =
        repository.setQuery(query)

    val searchResult: StateFlow<Result<SearchResponse>> = repository
        .searchUser()
        .stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            Result.error(message = "type something to discover new github friends", Error())
        )

    private val _username = MutableStateFlow("")
    fun setSelectedUsername(newUsername: String) {
        _username.value = newUsername
    }

    val detailUser: StateFlow<Result<UserDetail>> = _username
        .distinctUntilChanged { old, new -> old == new && new == "" }
        .transformLatest {
            repository
                .getDetailUserBy(it)
                .collect { result ->
                    emit(result)
                }

        }.stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            Result.loading()
        )
    val followerUser: StateFlow<Result<List<UserSearch>>> = _username
        .distinctUntilChanged { old, new -> old == new && new == "" }
        .transformLatest {
            repository.getFollowerUserBy(it)
                .collect { result ->
                    emit(result)
                }

        }.stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            Result.loading()
        )

    val followingUser: StateFlow<Result<List<UserSearch>>> = _username
        .distinctUntilChanged { old, new -> old == new && new == "" }
        .transformLatest {
            repository.getFollowingUserBy(it)
                .collect { result ->
                    emit(result)
                }

        }.stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            Result.loading()
        )
}