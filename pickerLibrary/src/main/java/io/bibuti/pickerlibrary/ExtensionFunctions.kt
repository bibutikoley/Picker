package io.bibuti.pickerlibrary

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import android.webkit.MimeTypeMap
import io.bibuti.pickerlibrary.ConstantsHolder.TAG
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception
import java.util.*


object ConstantsHolder {
    const val TAG = "ExtensionFunctions.kt"
    const val ACTIVITY_RESULT_REQUEST_CODE = 329
}

/**
 * This function returns File and MimeType form contentUri (contentUri is obtained from Intent.data from onActivityResult())
 */
fun Context.createFileFromContentUri(contentUri: Uri, onFileReady: (File, String?) -> Unit) {
    this.contentResolver?.let { contentResolver ->
        try {
            contentResolver.query(contentUri, null, null, null, null)?.use { cursor ->

                //Step 1: Obtain/Create file name with extension
                cursor.moveToFirst()
                var name = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                if (name.isNullOrEmpty()) {
                    MimeTypeMap.getSingleton().getExtensionFromMimeType(contentResolver.getType(contentUri))?.let { extension ->
                        name = UUID.randomUUID().toString().plus(".$extension")
                    }
                }
                if (name.isNullOrEmpty()) {
                    Log.e(TAG, "createFileFromContentUri: error creating name for the file")
                    return@let
                }

                //Step 2: Create a temp file
                val attachmentFile = File(cacheDir, name)
                //val attachmentFile = File(getAttachmentCacheDirectory().plus(name))
                this.contentResolver.openInputStream(contentUri)?.use { inputStream ->
                    FileOutputStream(attachmentFile).use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                    //file is now created and ready now..

                    var mimeType: String? = attachmentFile.toURI().toURL()?.openConnection()?.contentType
                    val mediaType = mimeType.toString().split("/").firstOrNull()
                    mediaType?.apply {
                        if (this == "application") {
                            mimeType = mediaType.plus("/").plus(attachmentFile.extension)
                        }
                    }

                    //Send File and mime type back to caller function
                    onFileReady.invoke(attachmentFile, mimeType)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

fun Context.createFileFromBitmap(bitmap: Bitmap, onFileReady: (File, String) -> Unit) {
    try {
        //Step 1: Create a temp file
        val attachmentFile = File(cacheDir, "${System.currentTimeMillis()}.png")
        FileOutputStream(attachmentFile).use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 80, outputStream)
            //Step 2: File is ready
            onFileReady.invoke(attachmentFile, "image/png")
        }
    } catch (e: IOException) {
        e.printStackTrace()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun Context.clearCache() {
    cacheDir.deleteRecursively()
}