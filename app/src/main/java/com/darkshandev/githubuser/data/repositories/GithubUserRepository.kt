package com.darkshandev.githubuser.data.repositories

import com.darkshandev.githubuser.data.models.ProfileUser
import com.google.gson.GsonBuilder

class GithubUserRepository {

    fun getGithubUserListFromJson(jsonString: String):List<ProfileUser>{
        return GsonBuilder().create().fromJson(jsonString, Array<ProfileUser>::class.java).toList()
    }
    companion object {
        var service: GithubUserRepository? = null

        fun getInstance() : GithubUserRepository {
            if (service == null) {
                service = GithubUserRepository()
            }
            return service!!
        }
    }
}