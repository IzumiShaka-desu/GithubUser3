package com.darkshandev.githubuser.presentation.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.darkshandev.githubuser.R
import com.darkshandev.githubuser.data.models.ProfileUser
import com.darkshandev.githubuser.data.repositories.GithubUserRepository
import com.darkshandev.githubuser.databinding.ActivityMainBinding
import com.darkshandev.githubuser.presentation.detail.DetailActivity
import com.darkshandev.githubuser.presentation.main.adapter.MainListAdapter
import com.darkshandev.githubuser.presentation.main.viewmodel.MainViewmodel
import com.darkshandev.githubuser.presentation.main.viewmodel.MainViewmodelFactory

class MainActivity : AppCompatActivity(), MainListAdapter.Listener {
    companion object {
        const val EXTRA_USER = "EXTRA_USER"
    }
private  lateinit var binding: ActivityMainBinding
    private lateinit var viewmodel: MainViewmodel
    private lateinit var adapterM: MainListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addInitialDataListener()
        initActivity()
    }

    private fun addInitialDataListener() {
        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener {
            return@addOnPreDrawListener true
        }
    }

    private fun initActivity() {
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adapterM = MainListAdapter(this)
        viewmodel = ViewModelProvider(
            this,
            MainViewmodelFactory(
                application,
                GithubUserRepository.getInstance()
            )
        ).get(MainViewmodel::class.java)
        binding.rvUserList.apply {
            layoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
            adapter = adapterM
        }

        viewmodel.userList.observe(this) {
            adapterM.updateUserList(it)

        }

        viewmodel.getAllUser()
        supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
        }
    }

    override fun onItemClickListener(view: View, user: ProfileUser) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(EXTRA_USER, user)
        startActivity(intent)
    }
}