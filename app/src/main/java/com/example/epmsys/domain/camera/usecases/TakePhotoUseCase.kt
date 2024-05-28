package com.example.epmsys.domain.camera.usecases

import android.content.Context
import android.net.Uri
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import com.example.epmsys.data.camera.util.Resource

interface TakePhotoUseCase {
    suspend fun captureAndSaveImage(context: Context): Resource<Uri?>
    suspend fun showCameraPreview(
        previewView: PreviewView,
        lifecycleOwner: LifecycleOwner
    )
}