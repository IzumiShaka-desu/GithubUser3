package com.darkshandev.githubuser.presentation.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.darkshandev.githubuser.databinding.ActivityMainBinding
import com.darkshandev.githubuser.presentation.main.viewmodel.MainViewmodel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_USER = "EXTRA_USER"
    }

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val mainViewmodel: MainViewmodel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initActivity()
    }


    private fun initActivity() {
        setContentView(binding.root)
    }


}