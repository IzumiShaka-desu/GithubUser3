package com.darkshandev.githubuser.presentation.main.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.darkshandev.githubuser.R
import com.darkshandev.githubuser.data.models.ProfileUser
import com.darkshandev.githubuser.databinding.ItemListBinding

class MainListAdapter: RecyclerView.Adapter<MainListAdapter.ViewHolder>() {

    var userList = listOf<ProfileUser>()

    fun updateUserList(newList: List<ProfileUser>){
        this.userList = newList

        notifyDataSetChanged()
        Log.d("kokkkk",this.userList.size.toString())
    }

    inner class ViewHolder(val binding: ItemListBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ItemListBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
//        val inflater =  LayoutInflater.from(parent.context)
//        val binding = ItemListBinding.inflate(inflater,parent,false)
//        return ViewHolder(binding)
//
//    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.profile = userList[position]
        Log.d("posisi",userList[position].name+position.toString())
    }

    override fun getItemCount(): Int = userList.size

    interface  Listener{
        fun onItemClickListener(view: View, user: ProfileUser)
    }
}