package com.freelanxer.ktpermissionutil.activity

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.freelanxer.ktpermissionutil.R
import com.freelanxer.ktpermissionutil.utils.PermissionUtil

abstract class BaseActivity: AppCompatActivity() {

    private val mPermissionUtil = lazy {
        PermissionUtil(this)
    }

    protected fun hasAllPermission(): Boolean =
        mPermissionUtil.value.hasAllPermission()

    protected fun hasCameraPermission(): Boolean =
        mPermissionUtil.value.hasCameraPermission()

    protected fun hasStoragePermission(): Boolean =
        mPermissionUtil.value.hasStoragePermission()

    protected fun hasNotificationPermission(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU)
            return false
        return mPermissionUtil.value.hasNotificationPermission()
    }

    private fun showPermissionRequestAlert(messageResId: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(messageResId)
        builder.setPositiveButton(R.string.settings) { dialog, which -> navigatePermissionSettings() }
        builder.setNegativeButton(R.string.cancel, null)
        builder.show()
    }

    private fun navigatePermissionSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri = Uri.fromParts("package", packageName, null)
        intent.setData(uri)
        startActivity(intent)
    }

    private fun onPermissionGrantResult(
        permissions: Array<out String>,
        grantResults: IntArray,
        messageResId: Int,
    ) {
        for (i in permissions.indices) {
            val permissionName = permissions[i]
            val resultCode = grantResults[i]
            if (resultCode == PackageManager.PERMISSION_DENIED && !ActivityCompat.shouldShowRequestPermissionRationale(this, permissionName)) {
                showPermissionRequestAlert(messageResId)
                break
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PermissionUtil.REQUEST_CODE_PERMISSION_CAMERA -> {
                onPermissionGrantResult(permissions, grantResults, R.string.permission_camera)
            }
            PermissionUtil.REQUEST_CODE_PERMISSION_STORAGE -> {
                onPermissionGrantResult(permissions, grantResults, R.string.permission_storage)
            }
        }
    }

}