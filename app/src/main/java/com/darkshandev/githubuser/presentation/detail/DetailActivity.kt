package com.darkshandev.githubuser.presentation.detail

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.darkshandev.githubuser.MainActivity
import com.darkshandev.githubuser.data.models.ProfileUser
import com.darkshandev.githubuser.databinding.ActivityDetailBinding
import com.darkshandev.githubuser.utils.getBitmapFromView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


class DetailActivity : AppCompatActivity() {
    val TITLE = "Profile"
    private lateinit var binding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        intent.getParcelableExtra<ProfileUser>(MainActivity.EXTRA_USER)?.apply {
            binding.detailProfile = this
        }
        supportActionBar?.apply {
            title = TITLE
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
        binding.fabShare.setOnClickListener {
            GlobalScope.launch { sharePage(binding.root) }
        }
    }

    private suspend fun sharePage(view: View) {
        val bitmap = getBitmapFromView(view)
        val cachePath = File(externalCacheDir, "my_images/")
        cachePath.mkdirs()
        val file = File(cachePath, "Image_123.png")
        val fileOutputStream: FileOutputStream
        try {
            withContext(Dispatchers.IO) {
                fileOutputStream = FileOutputStream(file)
                bitmap?.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
                fileOutputStream.flush()
                fileOutputStream.close()
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val myImageFileUri: Uri =
            FileProvider.getUriForFile(this, applicationContext.packageName + ".provider", file)
        withContext(Dispatchers.Main) {
            val intent = Intent(Intent.ACTION_SEND)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.putExtra(Intent.EXTRA_STREAM, myImageFileUri)
            intent.putExtra(Intent.EXTRA_STREAM, myImageFileUri)
            intent.type = "image/png"
            startActivity(Intent.createChooser(intent, "Share with"))
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}