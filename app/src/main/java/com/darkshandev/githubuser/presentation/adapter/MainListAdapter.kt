package com.darkshandev.githubuser.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.darkshandev.githubuser.data.models.UserSearch
import com.darkshandev.githubuser.databinding.ItemListBinding
import com.darkshandev.githubuser.utils.UserDiffUtils

class MainListAdapter(private val listener: Listener) :
    RecyclerView.Adapter<MainListAdapter.ViewHolder>() {

    private var userList = emptyList<UserSearch>()

    fun updateUserList(newList: List<UserSearch>) {
        val diff = DiffUtil
            .calculateDiff(UserDiffUtils(userList, newList))
        this.userList = newList

        diff.dispatchUpdatesTo(this)
    }

    inner class ViewHolder(val binding: ItemListBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ItemListBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.profile = userList[position]
        holder.binding.root.setOnClickListener {
            listener.onItemClickListener(it, userList[position])
        }
    }

    override fun getItemCount(): Int = userList.size

    interface Listener {
        fun onItemClickListener(view: View, user: UserSearch)
    }
}