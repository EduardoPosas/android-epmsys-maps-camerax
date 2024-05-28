package com.example.epmsys.ui.point

import android.content.Context
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.epmsys.data.camera.util.Resource
import com.example.epmsys.domain.camera.repository.CameraRepository
import com.example.epmsys.domain.camera.usecases.TakePhotoUseCase
import com.example.epmsys.domain.location.LocationTrackerRepository
import com.example.epmsys.domain.point.OfflinePointRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class PointViewModel @Inject constructor(
    private val locationTrackerRepository: LocationTrackerRepository,
    private val offlinePointRepository: OfflinePointRepository,
    private val takePhotoUseCase: TakePhotoUseCase,
    private val cameraRepository: CameraRepository
) : ViewModel() {
    private var _pointUiState = MutableStateFlow(PointUiState())
    val pointUiState: StateFlow<PointUiState> = _pointUiState.asStateFlow()

    val isPhotoTaken: StateFlow<Boolean> = cameraRepository.isPhotoTaken

    init {
        _pointUiState.update { currentState ->
            currentState.copy(
                pointDetails = currentState.pointDetails.copy(dateTime = getCurrentDate())
            )
        }
    }

    fun updatePointUiState(pointDetails: PointDetails) {
        _pointUiState.value = PointUiState(
            pointDetails = pointDetails
        )
        validatePointEntry()
    }

    suspend fun getCurrentLocation() {
        viewModelScope.launch {
            val currentLocation = locationTrackerRepository.getCurrentLocation()
            if (currentLocation != null) {
                _pointUiState.update { currentState ->
                    currentState.copy(
                        pointDetails = currentState.pointDetails.copy(
                            latitude = currentLocation.latitude,
                            longitude = currentLocation.longitude
                        )
                    )
                }
                validatePointEntry()
            }
        }
    }

    fun captureAndSaveImage(context: Context) {
        viewModelScope.launch {
            val result = takePhotoUseCase.captureAndSaveImage(context)
            when (result) {
                is Resource.Error -> {
                    _pointUiState.update { currentState ->
                        currentState.copy(
                            pointDetails = currentState.pointDetails.copy(photoUrl = "")
                        )
                    }
                }

                is Resource.Success -> {
                    _pointUiState.update { currentState ->
                        currentState.copy(
                            pointDetails = currentState.pointDetails.copy(photoUrl = result.uri.toString())
                        )
                    }
                    validatePointEntry()
                }
            }
        }
    }

    fun showCameraPreview(
        previewView: PreviewView,
        lifecycleOwner: LifecycleOwner
    ) {
        viewModelScope.launch {
            takePhotoUseCase.showCameraPreview(
                previewView,
                lifecycleOwner
            )
        }
    }

    suspend fun savePointEntry() {
        viewModelScope.launch {
            validatePointEntry()
            if (pointUiState.value.isValidEntry) {
                offlinePointRepository.AddPoint(pointUiState.value.pointDetails.toPointEntity())
            }
        }
    }


    private fun getCurrentDate(): String {
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return currentDateTime.format(formatter)
    }

    fun updateDate() {
        _pointUiState.update { currentState ->
            currentState.copy(
                pointDetails = currentState.pointDetails.copy(dateTime = getCurrentDate()),
            )
        }
        validatePointEntry()
    }

    private fun validatePointEntry(pointDetails: PointDetails = pointUiState.value.pointDetails) {
        val errors = mutableMapOf<String, String>()

        if (pointDetails.category.isBlank()) {
            errors["category"] = "* Category is Required"
        }

        if (pointDetails.action.isBlank()) {
            errors["action"] = "* Action is Required"
        }

        if (pointDetails.dateTime.isBlank()) {
            errors["dateTime"] = "* Update the date"
        }

        if (pointDetails.latitude == 0.0 || pointDetails.longitude == 0.0) {
            errors["location"] = "* Update location"
        }

        if (pointDetails.photoUrl.isBlank()) {
            errors["photo"] = "* Photo Required"
        }

        if (errors.isNotEmpty()) {
            _pointUiState.update { currentState ->
                currentState.copy(errors = errors)
            }
            return
        }
        _pointUiState.update { currentState ->
            currentState.copy(isValidEntry = true)
        }
    }

    fun resetIsPhotoTaken() {
        cameraRepository.resetIsPhotoTaken()
    }

}
