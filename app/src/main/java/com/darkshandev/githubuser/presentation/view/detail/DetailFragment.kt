package com.darkshandev.githubuser.presentation.detail

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.darkshandev.githubuser.databinding.FragmentDetailBinding
import com.darkshandev.githubuser.presentation.main.viewmodel.MainViewmodel
import com.darkshandev.githubuser.utils.getBitmapFromView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class DetailFragment : Fragment() {
    private lateinit var binding: FragmentDetailBinding
    private val mainViewModel: MainViewmodel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        binding.fabShare.setOnClickListener {
            lifecycleScope.launch { sharePage(binding.root) }
        }

        return binding.root

    }
private fun showLoading(isLoading:Boolean){
    binding.apply {
        progressBar.visibility=if(isLoading)View.VISIBLE else View.GONE
        detailLayout.visibility=if(isLoading)View.GONE else View.VISIBLE
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
        if (context==null){
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