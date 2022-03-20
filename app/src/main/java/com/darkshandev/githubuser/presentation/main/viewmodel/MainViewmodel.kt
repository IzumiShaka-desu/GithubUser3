package com.darkshandev.githubuser.presentation.main.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.darkshandev.githubuser.data.models.ProfileUser
import com.darkshandev.githubuser.data.repositories.GithubUserRepository
import com.darkshandev.githubuser.utils.jsonLoader

class MainViewmodel constructor(
    private val application: Application,
    private val repository: GithubUserRepository
) : ViewModel() {

    val userList = MutableLiveData<List<ProfileUser>>()

    fun getAllUser() {
        val jsonString: String = jsonLoader(application, "users.json")
        val users = repository.getGithubUserListFromJson(jsonString)
        userList.postValue(users)

    }

}