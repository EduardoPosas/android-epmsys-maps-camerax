package com.example.epmsys.di

import android.app.Application
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.room.Room
import coil.ImageLoader
import com.example.epmsys.data.camera.repository.CameraRepositoryImpl
import com.example.epmsys.data.camera.usecases.TakePhotoUseCaseImpl
import com.example.epmsys.data.point.PointDatabase
import com.example.epmsys.domain.camera.repository.CameraRepository
import com.example.epmsys.domain.camera.usecases.TakePhotoUseCase
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFusedLocationProviderClient(app: Application): FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(app)

    @Provides
    @Singleton
    fun providePointDatabase(app: Application): PointDatabase {
        return Room.databaseBuilder(
            app,
            PointDatabase::class.java,
            "point_db"
        ).build()
    }


    @Provides
    @Singleton
    fun provideCameraProvider(app: Application): ProcessCameraProvider {
        return ProcessCameraProvider.getInstance(app).get()
    }

    @Provides
    @Singleton
    fun provideCameraPreview(): Preview {
            return Preview.Builder().build()
    }

    @Provides
    @Singleton
    fun provideCameraSelector(): CameraSelector {
        return CameraSelector.DEFAULT_BACK_CAMERA
    }

    @Provides
    @Singleton
    fun provideImageCapture(): ImageCapture {
        return ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY) // default
            .setFlashMode(ImageCapture.FLASH_MODE_OFF) // controls for taking pictures
            .build()
    }

    @Provides
    @Singleton
    fun provideCameraRepository(
        cameraProvider: ProcessCameraProvider,
        selector: CameraSelector,
        preview: Preview,
        imageCapture: ImageCapture
    ): CameraRepository {
        return CameraRepositoryImpl(cameraProvider, selector, preview, imageCapture)
    }

    @Provides
    @Singleton
    fun provideTakePhotoUseCase(cameraRepository: CameraRepository): TakePhotoUseCase {
        return TakePhotoUseCaseImpl(cameraRepository)
    }

}