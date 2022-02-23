package com.darkshandev.githubuser.presentation.main.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.darkshandev.githubuser.data.models.ProfileUser
import com.darkshandev.githubuser.databinding.ItemListBinding

class MainListAdapter(private val listener: Listener) :
    RecyclerView.Adapter<MainListAdapter.ViewHolder>() {

    var userList = ArrayList<ProfileUser>()

    fun updateUserList(newList: List<ProfileUser>) {
        this.userList.addAll(newList)

        notifyDataSetChanged()
        Log.d("kokkkk", this.userList.size.toString())
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
        Log.d("posisi", userList[position].name + position.toString())
    }

    override fun getItemCount(): Int = userList.size

    interface Listener {
        fun onItemClickListener(view: View, user: ProfileUser)
    }
}