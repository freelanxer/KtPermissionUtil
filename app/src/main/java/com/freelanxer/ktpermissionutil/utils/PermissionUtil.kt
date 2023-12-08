package com.freelanxer.ktpermissionutil.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat

class PermissionUtil(private val activity: Activity) {
    companion object {
        const val REQUEST_CODE_PERMISSION_ALL = 9000
        const val REQUEST_CODE_PERMISSION_CAMERA = 9001
        const val REQUEST_CODE_PERMISSION_STORAGE = 9002
        const val REQUEST_CODE_PERMISSION_NOTIFICATION = 9003
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private val allPermissionApi33 = arrayOf(
        Manifest.permission.POST_NOTIFICATIONS,
        Manifest.permission.CAMERA,
        Manifest.permission.READ_MEDIA_IMAGES,
        Manifest.permission.READ_MEDIA_VIDEO,
    )

    private val allPermission = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
    )

    private val cameraPermission = arrayOf(Manifest.permission.CAMERA)

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private val notificationPermission = arrayOf(Manifest.permission.POST_NOTIFICATIONS)

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private val storagePermissionApi33 = arrayOf(
        Manifest.permission.READ_MEDIA_IMAGES,
        Manifest.permission.READ_MEDIA_VIDEO,
    )

    private val storagePermission = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
    )

    fun hasAllPermission(): Boolean {
        val permissions =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                allPermissionApi33
            else
                allPermission
        if (!hasPermissions(activity, permissions)) {
            ActivityCompat.requestPermissions(activity, permissions, REQUEST_CODE_PERMISSION_ALL)
            return false
        }
        return true
    }

    fun hasCameraPermission(): Boolean {
        if (!hasPermissions(activity, cameraPermission)) {
            ActivityCompat.requestPermissions(activity, cameraPermission, REQUEST_CODE_PERMISSION_CAMERA)
            return false
        }
        return true
    }

    fun hasStoragePermission(): Boolean {
        val permissions =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                storagePermissionApi33
            else
                storagePermission
        if (!hasPermissions(activity, permissions)) {
            ActivityCompat.requestPermissions(activity, permissions, REQUEST_CODE_PERMISSION_STORAGE)
            return false
        }
        return true
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun hasNotificationPermission(): Boolean {
        if (!hasPermissions(activity, notificationPermission)) {
            ActivityCompat.requestPermissions(activity, notificationPermission, REQUEST_CODE_PERMISSION_NOTIFICATION)
            return false
        }
        return true
    }

    private fun hasPermissions(context: Context, permissions: Array<String>): Boolean {
        if (permissions.isEmpty())
            return false
        for (permission in permissions) {
            if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED)
                return false
        }
        return true
    }

}