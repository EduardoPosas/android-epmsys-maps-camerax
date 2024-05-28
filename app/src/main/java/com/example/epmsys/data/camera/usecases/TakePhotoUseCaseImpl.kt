package com.example.epmsys.data.camera.usecases

import android.content.Context
import android.net.Uri
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import com.example.epmsys.data.camera.util.Resource
import com.example.epmsys.domain.camera.repository.CameraRepository
import com.example.epmsys.domain.camera.usecases.TakePhotoUseCase
import javax.inject.Inject

class TakePhotoUseCaseImpl @Inject constructor(
    private val cameraRepository: CameraRepository
) : TakePhotoUseCase {
    override suspend fun captureAndSaveImage(context: Context): Resource<Uri?> = cameraRepository.captureAndSaveImage(context)

    override suspend fun showCameraPreview(
        previewView: PreviewView,
        lifecycleOwner: LifecycleOwner
    ) = cameraRepository.showCameraPreview(previewView,lifecycleOwner)
}