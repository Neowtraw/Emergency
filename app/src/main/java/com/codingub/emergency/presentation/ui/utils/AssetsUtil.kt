package com.codingub.emergency.presentation.ui.utils

import android.content.Context
import android.util.Log
import com.codingub.emergency.presentation.ui.utils.AssetUtil.FileExtension.TXT
import java.io.IOException
import java.io.InputStream


object AssetUtil {
    private fun assetPath(filePath: String, fileExt: String): String {
        Log.d("", "file:///android_asset/$filePath.$fileExt")
        return "file:///android_asset/$filePath.$fileExt"
    }

    fun getText(context: Context, name: String): String {
        //  val assetPath = assetPath(name, TXT)

        //  return File("file:///android_asset/algorithm_help.txt").readText()


        val assetManager = context.assets

        return try {
            val inputStream: InputStream = assetManager.open("$name.$TXT")
            inputStream.bufferedReader().use { it.readText() }
        } catch (e: IOException) {
            ""
        }
    }

    private object FileExtension {
        const val TXT: String = "txt"
    }
}