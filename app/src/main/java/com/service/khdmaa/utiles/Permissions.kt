package com.service.khdmaa.utiles

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat

object Permissions {

    val cameraRequest: Int = 10
    val galleryRequest: Int = 11
    val MapRequest: Int = 15
    val cameraManifestPermissionsList = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    val galleryManifestPermissionsList = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    val locationManifestPermissionsList = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)


    fun checkCameraPermissions(context: Context): Boolean {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED || (
                    ActivityCompat.checkSelfPermission(
                        (context),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED)
        ) {
            return false
        }
        return true
    }

    fun checkLocationPermissions(context: Context): Boolean {
        if (ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    context as Activity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) || ActivityCompat.shouldShowRequestPermissionRationale(
                    context as Activity,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            ) {
                return false
            } else {
                return false
            }

        }
        return true

    }


    fun showPermissionsDialog(
        context: Context?,
        message: String?,
        permissionsArray: Array<String>,
        requestID: Int
    ) {
        val alertBuilder = AlertDialog.Builder(context)
        alertBuilder.setCancelable(false)
        alertBuilder.setTitle("Permission necessary")
        alertBuilder.setMessage(message)
         //request permission to allow
        alertBuilder.setPositiveButton(
            android.R.string.ok
        ) { dialog: DialogInterface?, which: Int ->
            ActivityCompat.requestPermissions(
                (context as Activity?)!!,
                permissionsArray!!,
                requestID
            )
        }
        val alert = alertBuilder.create()
        alert.show()
    }
}