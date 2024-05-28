package com.example.epmsys.domain.camera.repository

import android.content.Context
import android.net.Uri
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import com.example.epmsys.data.camera.util.Resource
import kotlinx.coroutines.flow.StateFlow

interface CameraRepository {
    val isPhotoTaken: StateFlow<Boolean>
    suspend fun captureAndSaveImage(context: Context): Resource<Uri?>
    suspend fun showCameraPreview(
        previewView: PreviewView,
        lifecycleOwner: LifecycleOwner
    )
    fun resetIsPhotoTaken()
}