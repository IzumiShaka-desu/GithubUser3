package com.darkshandev.githubuser.presentation.view

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AlertDialog.Builder
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.darkshandev.githubuser.R
import com.darkshandev.githubuser.data.models.Result
import com.darkshandev.githubuser.data.models.UserSearch
import com.darkshandev.githubuser.databinding.FragmentSearchBinding
import com.darkshandev.githubuser.presentation.adapter.MainListAdapter
import com.darkshandev.githubuser.presentation.viewmodel.MainViewmodel
import kotlinx.coroutines.launch


class SearchFragment : Fragment(), MainListAdapter.Listener {
    private val mainViewModel: MainViewmodel by activityViewModels()
    private val adapterM by lazy { MainListAdapter(this) }
    private val navController: NavController by lazy {
        Navigation.findNavController(activity = activity as Activity, R.id.viewContainer)
    }
    private var binding: FragmentSearchBinding? = null
    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        setupUi()
        setupObserver()
        return binding?.root
    }

    private fun setupObserver() {
        lifecycleScope.launch {
            mainViewModel.searchResult
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { result ->
                    when (result.status) {
                        Result.Status.LOADING -> {
                            binding?.apply {
                                progressbar.visibility = View.VISIBLE
                                rvSearchUser.visibility = View.GONE
                                errorSearchTv.visibility = View.GONE
                            }
                        }
                        Result.Status.ERROR -> {
                            binding?.apply {
                                errorSearchTv.text = result.message
                                progressbar.visibility = View.GONE
                                rvSearchUser.visibility = View.GONE
                                errorSearchTv.visibility = View.VISIBLE
                            }
                        }
                        Result.Status.SUCCESS -> {
                            binding?.apply {
                                progressbar.visibility = View.GONE
                                val newList = result.data?.items ?: emptyList()
                                if (newList.isEmpty()) {
                                    rvSearchUser.visibility = View.GONE
                                    errorSearchTv.text = "Cannot find any user you want"
                                    errorSearchTv.visibility = View.VISIBLE
                                } else {
                                    adapterM.updateUserList(newList)
                                    rvSearchUser.visibility = View.VISIBLE
                                    errorSearchTv.visibility = View.GONE
                                }

                            }
                        }
                    }
                }

        }
    }

    private fun setupUi() {

        binding?.apply {
            rvSearchUser.adapter = adapterM
            searchField.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean = true
                override fun onQueryTextChange(newText: String): Boolean {
                    mainViewModel.setQuery(newText)
                    return true
                }
            })
            toolbarSearch.setupWithNavController(
                navController = navController,
                configuration = AppBarConfiguration(
                    setOf(
                        R.id.searchFragment,
                        R.id.detailFragment,
                    )
                )
            )
            NavigationUI.setupWithNavController(toolbarSearch, navController)
            toolbarSearch.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.action_favorite -> {
                        navController.navigate(R.id.navigate_to_fav)
                        true
                    }
                    R.id.action_setting -> {
                        popDialog()
                        true
                    }
                    else -> {
                        false
                    }
                }
            }
        }
    }

    private fun popDialog() {
        context?.let {
            val modes = arrayOf("Dark mode", "Day mode")

            val builder: AlertDialog.Builder = Builder(it)
            builder.setTitle("Select Theme Mode")
            builder.setItems(modes) { _, which ->
                mainViewModel.saveThemeSetting(which == 0)
                Toast.makeText(it, "apply " + modes[which], Toast.LENGTH_SHORT).show()
            }
            builder.show()
        }

    }

    override fun onItemClickListener(view: View, user: UserSearch) {
        mainViewModel.setSelectedUsername(user.login)
        navController.navigate(R.id.navigate_to_detail)
    }
}


