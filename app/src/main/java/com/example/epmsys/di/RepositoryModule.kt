package com.example.epmsys.di

import com.example.epmsys.data.camera.usecases.SavePhotoToGalleryUseCase
import com.example.epmsys.data.location.LocationTrackerRepositoryImpl
import com.example.epmsys.data.point.OfflinePointRepositoryImpl
import com.example.epmsys.domain.location.LocationTrackerRepository
import com.example.epmsys.domain.point.OfflinePointRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindLocationTrackerRepository(
        locationTrackerRepository: LocationTrackerRepositoryImpl
    ): LocationTrackerRepository


    @Binds
    @Singleton
    abstract fun bindOfflinePointRepository(
        offlinePointRepository: OfflinePointRepositoryImpl
    ): OfflinePointRepository

}