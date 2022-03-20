package com.darkshandev.githubuser.utils

import androidx.recyclerview.widget.DiffUtil
import com.darkshandev.githubuser.data.models.UserSearch

class UserDiffUtils(private val oldList: List<UserSearch>, private val newList: List<UserSearch>) :
    DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList == newList

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old = oldList[oldItemPosition]
        val latest = newList[newItemPosition]
        return when {
            old.id == latest.id -> true
            old.login == latest.login -> true
            else -> false
        }
    }
}