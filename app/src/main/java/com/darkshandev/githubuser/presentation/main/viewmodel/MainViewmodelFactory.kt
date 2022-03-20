package com.darkshandev.githubuser.presentation.main.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.darkshandev.githubuser.data.repositories.GithubUserRepository


class MainViewmodelFactory(
    private val application: Application,
    private val repository: GithubUserRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MainViewmodel::class.java)) {
            MainViewmodel(application, repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }

}