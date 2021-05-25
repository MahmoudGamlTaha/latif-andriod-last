package com.latifapp.latif.utiles

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream

fun Uri.getRealPathFromGallery(activity: Context?):String{
    try {
        val imageStream: InputStream =
            activity!!.getContentResolver().openInputStream(this)!!
        var selectedImage = BitmapFactory.decodeStream(imageStream)
        val angle: Int = getRotateImage(this.getPath())
        val matrix = Matrix()
        matrix.postRotate(angle.toFloat())
        selectedImage = Bitmap.createBitmap(
            selectedImage,
            0,
            0,
            selectedImage.width,
            selectedImage.height,
            matrix,
            false
        )
        var width = selectedImage.width
        var height = selectedImage.height
        if (width > 900) {
            height = (height * (900f / width)).toInt()
            width = 900
        }
        selectedImage = Bitmap.createScaledBitmap(selectedImage, width, height, false)
        var  uri = getImageUri(activity, selectedImage)
        return getRealPathFromURI(uri, activity!!)!!
    } catch (e: Exception) {
        e.printStackTrace()
        Utiles.log_D("mvmvmvmmvmv", e.message!!)
    }
    return ""
}

fun getRotateImage(path: String?): Int {
    var ei: ExifInterface? = null
    try {
        ei = ExifInterface(path!!)
        val orientation: Int = ei.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_UNDEFINED
        )
        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90
            ExifInterface.ORIENTATION_ROTATE_180 -> 180
            ExifInterface.ORIENTATION_ROTATE_270 -> 270
            ExifInterface.ORIENTATION_NORMAL -> 0
            else -> 0
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return 0
}

fun getImageUri(activity: Context?, selectedImage: Bitmap?): Uri? {
    val bytes = ByteArrayOutputStream()
    val path: String =
        MediaStore.Images.Media.insertImage(activity!!.getContentResolver(), selectedImage, "Title", null)
    return Uri.parse(path)
}

fun getRealPathFromURI(
    contentURI: Uri?,
    activity: Context
): String? {
    if (contentURI == null) return ""
    val cursor =
        activity.contentResolver.query(contentURI, null, null, null, null)
    return if (cursor == null) {
        Utiles.log_D("MediaStoreMediaStore2", contentURI.path!!)
        contentURI.path
    } else {
        cursor.moveToFirst()
        val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
        Utiles.log_D("MediaStoreMediaStore3", cursor.getString(idx))
        val path = cursor.getString(idx)
        cursor.close()
        path
    }


}