package com.darkshandev.githubuser.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.darkshandev.githubuser.data.datasources.SettingPreferences
import com.darkshandev.githubuser.data.models.*
import com.darkshandev.githubuser.data.repositories.GithubUserRepository
import com.darkshandev.githubuser.utils.toEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewmodel @Inject constructor(
    private val repository: GithubUserRepository,
    private val pref: SettingPreferences
) : ViewModel() {

    val themeSettings: StateFlow<Boolean> = pref.getThemeSetting().stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        false
    )


    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }

    fun setQuery(query: String) =
        repository.setQuery(query)

    @FlowPreview
    @ExperimentalCoroutinesApi
    val searchResult: StateFlow<Result<SearchResponse>> = repository
        .searchUser()
        .stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            Result.error(message = "type something to discover new github friends", Error())
        )

    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username
    fun setSelectedUsername(newUsername: String) {
        _username.value = newUsername
    }

    @ExperimentalCoroutinesApi
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

    @ExperimentalCoroutinesApi
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

    @ExperimentalCoroutinesApi
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

    @ExperimentalCoroutinesApi
    val favoriteUser: StateFlow<List<UserEntity>> = repository
        .getFavoriteUser()
        .stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            emptyList()
        )


    @ExperimentalCoroutinesApi
    fun addOrRemoveFavorite() {
        detailUser.value.data?.let { detail ->
            viewModelScope.launch {
                val isFavourited = favoriteUser.value.any { it.login == username.value }
                if (isFavourited) {
                    repository.removeFavorite(detail.toEntity())
                } else {
                    repository.addFavorite(detail.toEntity())
                }

            }

        }

    }
}