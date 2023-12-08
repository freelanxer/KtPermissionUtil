package com.freelanxer.ktpermissionutil.activity

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.freelanxer.ktpermissionutil.R
import java.io.File

class MainActivity : BaseActivity(), View.OnClickListener {
    private val cameraBtn = lazy { findViewById<Button>(R.id.camera_btn) }
    private val pickerBtn = lazy { findViewById<Button>(R.id.picker_btn) }
    private lateinit var photoIv: ImageView
    private lateinit var mPictureFile: File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindView()
        hasAllPermission()
    }

    private fun bindView() {
        photoIv = findViewById(R.id.photo_iv)
        cameraBtn.value.setOnClickListener(this)
        pickerBtn.value.setOnClickListener(this)
    }

    /**
     * 開啟相機
     */
    private fun launchCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).also {
            val cacheDirRoot = externalCacheDir ?: cacheDir
            val cacheFileDir = File(cacheDirRoot, "photos")
            if (!cacheFileDir.exists())
                cacheFileDir.mkdirs()
            mPictureFile = File("${cacheFileDir.absolutePath}/permission_util_${System.currentTimeMillis()}.jpg")
            val photoUri = FileProvider.getUriForFile(this, "${packageName}.fileprovider", mPictureFile)
            it.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        }
        cameraLauncher.launch(intent)
    }

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it?.resultCode != RESULT_OK)
            return@registerForActivityResult
        Glide.with(this)
            .load(mPictureFile.absolutePath)
            .into(photoIv)
    }

    /**
     * 開啟照片選擇器
     */
    private fun launchPhotoPicker() {
        if (!ActivityResultContracts.PickVisualMedia.isPhotoPickerAvailable(this))
            return
        pickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val pickerLauncher = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {
        it?.let {
            Glide.with(this).load(it).into(photoIv)
        }
    }

    private fun onCameraClicked() {
        if (!hasCameraPermission())
            return
        launchCamera()
    }

    private fun onPickerClicked() {
        if (!hasStoragePermission())
            return
        launchPhotoPicker()
    }

    override fun onClick(view: View?) {
        when(view?.id) {
            R.id.camera_btn -> onCameraClicked()
            R.id.picker_btn -> onPickerClicked()
        }
    }

}