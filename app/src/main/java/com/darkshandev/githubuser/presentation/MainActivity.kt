package com.darkshandev.githubuser.presentation

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.darkshandev.githubuser.databinding.ActivityMainBinding
import com.darkshandev.githubuser.presentation.viewmodel.MainViewmodel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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
        lifecycleScope.launch {
            mainViewmodel.themeSettings
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { result ->
                    setThemeMode(if (result) 1 else 0)
                }
        }
    }

    private fun setThemeMode(theme: Int) {
        val mode = when (theme) {
            0 -> AppCompatDelegate.MODE_NIGHT_NO
            1 -> AppCompatDelegate.MODE_NIGHT_YES
            else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }
        AppCompatDelegate.setDefaultNightMode(mode)
    }


}