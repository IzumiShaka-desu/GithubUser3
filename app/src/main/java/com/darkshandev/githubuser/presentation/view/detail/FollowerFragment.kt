package com.darkshandev.githubuser.presentation.view.detail

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
import com.darkshandev.githubuser.databinding.FragmentFollowerBinding
import com.darkshandev.githubuser.presentation.adapter.MainListAdapter
import com.darkshandev.githubuser.presentation.viewmodel.MainViewmodel
import kotlinx.coroutines.launch


class FollowerFragment : Fragment(), MainListAdapter.Listener {
    companion object {
        const val LABEL = "Follower"
    }

    private var binding: FragmentFollowerBinding? = null
    private val mainViewModel: MainViewmodel by activityViewModels()
    private val mAdapter by lazy { MainListAdapter(this) }
    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFollowerBinding.inflate(layoutInflater, container, false)
        binding?.apply {
            rvFollower.apply {
                adapter = mAdapter
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            }
        }
        initObserver()
        return binding?.root
    }

    private fun initObserver() {
        lifecycleScope.launch {
            mainViewModel.followerUser
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { result ->
                    when (result.status) {
                        Result.Status.LOADING -> {
                            binding?.apply {
                                errorFollower.visibility = View.GONE
                                rvFollower.visibility = View.GONE
                                shimmerLayoutFollower.visibility = View.VISIBLE
                                shimmerLayoutFollower.startShimmer()
                            }
                        }
                        Result.Status.ERROR -> {
                            binding?.apply {
                                shimmerLayoutFollower.stopShimmer()
                                shimmerLayoutFollower.visibility = View.GONE
                                errorFollower.text = result.message
                                rvFollower.visibility = View.GONE
                                errorFollower.visibility = View.VISIBLE
                            }
                        }
                        Result.Status.SUCCESS -> {
                            binding?.apply {
                                shimmerLayoutFollower.stopShimmer()
                                shimmerLayoutFollower.visibility = View.GONE
                                errorFollower.visibility = View.GONE
                                mAdapter.updateUserList(result.data ?: emptyList())
                                rvFollower.visibility = View.VISIBLE
                            }
                        }
                    }
                }
        }
    }

    override fun onItemClickListener(view: View, user: UserSearch) {

    }

}