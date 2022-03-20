package com.darkshandev.githubuser.presentation.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.darkshandev.githubuser.data.models.Result
import com.darkshandev.githubuser.data.models.UserSearch
import com.darkshandev.githubuser.databinding.FragmentFollowingBinding
import com.darkshandev.githubuser.presentation.main.adapter.MainListAdapter
import com.darkshandev.githubuser.presentation.main.viewmodel.MainViewmodel
import kotlinx.coroutines.launch

class FollowingFragment : Fragment(), MainListAdapter.Listener {
    private lateinit var binding: FragmentFollowingBinding
    private val mainViewModel: MainViewmodel by activityViewModels()
    private val mAdapter by lazy { MainListAdapter(this) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFollowingBinding.inflate(inflater, container, false)
        binding.apply {
            rvFollowing.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                adapter = mAdapter
            }

        }
        initObserver()
        return binding.root
    }

    private fun initObserver() {
        lifecycleScope.launch {
            mainViewModel.followerUser
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { result ->
                    when (result.status) {
                        Result.Status.LOADING -> {
                            binding.apply {
                                errorFollowing.visibility = View.GONE
                                rvFollowing.visibility = View.GONE
                                shimmerLayoutFollowing.visibility = View.VISIBLE
                                shimmerLayoutFollowing.startShimmer()
                            }
                        }
                        Result.Status.ERROR -> {
                            binding.apply {
                                shimmerLayoutFollowing.stopShimmer()
                                shimmerLayoutFollowing.visibility = View.GONE
                                errorFollowing.text = result.message
                                rvFollowing.visibility = View.GONE
                                errorFollowing.visibility = View.VISIBLE
                            }
                        }
                        Result.Status.SUCCESS -> {
                            binding.apply {
                                shimmerLayoutFollowing.stopShimmer()
                                shimmerLayoutFollowing.visibility = View.GONE
                                errorFollowing.visibility = View.GONE
                                mAdapter.updateUserList(result.data ?: emptyList())
                                rvFollowing.visibility = View.VISIBLE
                            }
                        }
                    }
                }
        }
    }

    override fun onItemClickListener(view: View, user: UserSearch) {

    }
}
