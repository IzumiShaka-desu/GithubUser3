package com.darkshandev.githubuser.presentation.view.detail

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.darkshandev.githubuser.R
import com.darkshandev.githubuser.data.models.Result
import com.darkshandev.githubuser.databinding.FragmentDetailBinding
import com.darkshandev.githubuser.presentation.adapter.TabViewPagerAdapter
import com.darkshandev.githubuser.presentation.viewmodel.MainViewmodel
import com.darkshandev.githubuser.utils.getBitmapFromView
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class DetailFragment : Fragment() {
    private var binding: FragmentDetailBinding? = null
    private val mainViewModel: MainViewmodel by activityViewModels()
    private val navController: NavController by lazy {
        Navigation.findNavController(activity = activity as Activity, R.id.viewContainer)
    }
    private val fragments = listOf<Fragment>(
        FollowerFragment(),
        FollowingFragment()
    )
    private var tabAdapter: TabViewPagerAdapter? = null
    override fun onDestroy() {
        binding = null
        tabAdapter = null
        super.onDestroy()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        setupUI()
        lifecycleScope.launch {
            mainViewModel.detailUser
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { result ->
                    when (result.status) {
                        Result.Status.LOADING -> {
                            binding?.apply {
                                progressBar.visibility = View.VISIBLE
                                detailLayout.visibility = View.GONE
                                errorTextDetail.visibility = View.GONE
                            }
                        }
                        Result.Status.ERROR -> {
                            binding?.apply {
                                progressBar.visibility = View.GONE
                                detailLayout.visibility = View.GONE
                                errorTextDetail.text = result.message
                                errorTextDetail.visibility = View.VISIBLE
                            }
                        }
                        Result.Status.SUCCESS -> {
                            binding?.apply {
                                progressBar.visibility = View.GONE
                                detailProfile = result.data
                                detailLayout.visibility = View.VISIBLE
                                errorTextDetail.visibility = View.GONE
                            }
                        }
                    }
                }
        }
        return binding?.root

    }

    private fun setupUI() {
        binding?.apply {
            toolbarDetail.setupWithNavController(
                navController = navController,
                configuration = AppBarConfiguration(
                    setOf(
                        R.id.searchFragment,
                        R.id.detailFragment,
                    )
                )
            )
            tabAdapter = TabViewPagerAdapter(fragments, childFragmentManager, lifecycle)
            vpagerDetail.adapter = tabAdapter
            TabLayoutMediator(tabs, vpagerDetail) { tab, position ->
                when (position) {
                    0 -> tab.text = FollowerFragment.LABEL
                    1 -> tab.text = FollowingFragment.LABEL
                }
            }.attach()
            (activity as? AppCompatActivity)?.let {

                it.setSupportActionBar(toolbarDetail)
                it.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            }
            toolbarDetail.setNavigationOnClickListener {
                activity?.onBackPressed()
            }
            fabShare.setOnClickListener {
                binding?.apply {
                    lifecycleScope.launch { sharePage(root) }
                }
            }
        }
    }


    private suspend fun sharePage(view: View) {
        val bitmap = getBitmapFromView(view)

        val cachePath = File(activity?.externalCacheDir, "my_images/")
        cachePath.mkdirs()
        val file = File(cachePath, "Image_123.png")
        val fileOutputStream: FileOutputStream
        try {
            withContext(Dispatchers.IO) {
                fileOutputStream = FileOutputStream(file)
                bitmap?.compress(
                    Bitmap.CompressFormat.PNG,
                    100,
                    fileOutputStream
                )
                fileOutputStream.flush()
                fileOutputStream.close()
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        if (context == null) {
            Toast.makeText(context, "cannot share please try again", Toast.LENGTH_SHORT).show()
            return
        }
        val myImageFileUri: Uri =
            FileProvider.getUriForFile(
                context!!,
                activity?.applicationContext?.packageName + ".provider",
                file
            )
        withContext(Dispatchers.Main) {
            val intent = Intent(Intent.ACTION_SEND)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.putExtra(Intent.EXTRA_STREAM, myImageFileUri)
            intent.putExtra(Intent.EXTRA_STREAM, myImageFileUri)
            intent.type = "image/png"
            startActivity(
                Intent.createChooser(
                    intent,
                    "Share with"
                )
            )
        }
    }

}