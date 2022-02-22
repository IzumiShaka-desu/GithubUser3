package com.darkshandev.githubuser.utils
import android.content.Context
import android.util.Log
import java.io.IOException

fun jsonLoader(context: Context,filename: String):String {
    var result: String="{}"
    try {
        val jsonString = context.assets.open(filename).bufferedReader().use { it.readText() }
        jsonString?.let {
            result=it
        }
    }catch (exception:IOException){
        exception.printStackTrace()
        Log.e("error coek","error coek "+exception.localizedMessage)
    }
    return  result
}