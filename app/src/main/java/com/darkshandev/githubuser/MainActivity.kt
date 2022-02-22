package com.darkshandev.githubuser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout.HORIZONTAL
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.darkshandev.githubuser.data.repositories.GithubUserRepository
import com.darkshandev.githubuser.presentation.main.adapter.MainListAdapter
import com.darkshandev.githubuser.presentation.main.viewmodel.MainViewmodel
import com.darkshandev.githubuser.presentation.main.viewmodel.MainViewmodelFactory

class MainActivity : AppCompatActivity() {
    lateinit var viewmodel: MainViewmodel
    val adapter: MainListAdapter = MainListAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewmodel = ViewModelProvider(
            this,
            MainViewmodelFactory(
                application,
                GithubUserRepository.getInstance()
            )
        ).get(MainViewmodel::class.java)
    val layoutManager =LinearLayoutManager(this)
        findViewById<RecyclerView>(R.id.rv_user_list).apply {
            layoutManager.orientation = LinearLayoutManager.HORIZONTAL
            this.layoutManager = layoutManager
            this.adapter = adapter
        }
        viewmodel.userList.observe(this, Observer {
            adapter.updateUserList(it)
            Log.d("data",it.toString())
        })

        viewmodel.getAllUser()
    }
}