package com.darkshandev.githubuser.presentation.view

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.darkshandev.githubuser.R
import com.darkshandev.githubuser.data.models.UserSearch
import com.darkshandev.githubuser.databinding.FragmentFavoriteBinding
import com.darkshandev.githubuser.presentation.adapter.MainListAdapter
import com.darkshandev.githubuser.presentation.viewmodel.MainViewmodel
import com.darkshandev.githubuser.utils.toListModelSearch
import kotlinx.coroutines.launch

class FavoriteFragment : Fragment(), MainListAdapter.Listener {

    private var binding: FragmentFavoriteBinding? = null
    private val mainViewModel: MainViewmodel by activityViewModels()
    private val mAdapter by lazy { MainListAdapter(this) }
    private val navController: NavController by lazy {
        Navigation.findNavController(activity = activity as Activity, R.id.viewContainer)
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        binding?.apply {
            rvFav.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                adapter = mAdapter
            }
            toolbarFav.apply {
                setupWithNavController(
                    navController = navController,
                    configuration = AppBarConfiguration(
                        setOf(
                            R.id.searchFragment,
                            R.id.detailFragment,
                            R.id.favoriteFragment
                        )
                    )
                )
                setNavigationOnClickListener {
                    activity?.onBackPressed()
                }
            }
            NavigationUI.setupWithNavController(toolbarFav, navController)
            toolbarFav.setNavigationOnClickListener {
                activity?.onBackPressed()
            }

        }
        initObserver()
        return binding?.root
    }

    private fun initObserver() {
        lifecycleScope.launch {
            mainViewModel.favoriteUser
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { result ->
                    mAdapter.updateUserList(result.toListModelSearch())
                }
        }
    }

    override fun onItemClickListener(view: View, user: UserSearch) {
        mainViewModel.setSelectedUsername(user.login)
        navController.navigate(R.id.navigate_to_detailfav)
    }


}