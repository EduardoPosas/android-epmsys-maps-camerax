package com.example.epmsys.ui.map

import android.location.Location
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.epmsys.data.point.toPointDetails
import com.example.epmsys.domain.location.LocationTrackerRepository
import com.example.epmsys.domain.point.OfflinePointRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val locationTrackerRepository: LocationTrackerRepository,
    private val offlinePointRepository: OfflinePointRepository
) : ViewModel() {

    var currentLocation = mutableStateOf<Location?>(null)
    val mapUiState: StateFlow<MapUiState> = offlinePointRepository.getAllPoints()
        .map { listPointEntity ->
            MapUiState(listPointEntity.map { it.toPointDetails() })
        }
        .stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            MapUiState()
        )

    init {
        getCurrentLocation()
    }

    fun getCurrentLocation() {
        viewModelScope.launch {
            currentLocation.value = locationTrackerRepository.getCurrentLocation()
        }
    }

}