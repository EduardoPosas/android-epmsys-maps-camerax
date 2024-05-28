package com.example.epmsys.ui.point

import com.example.epmsys.data.point.PointEntity

data class PointDetails(
    val category: String = "",
    val action: String = "",
    val dateTime: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val photoUrl: String = ""
)

data class PointUiState(
    val pointDetails: PointDetails = PointDetails(),
    val errors: Map<String, String> = emptyMap(),
    val isValidEntry: Boolean = false
)


fun PointDetails.toPointEntity() = PointEntity(
    category = category,
    action = action,
    dateTime = dateTime,
    latitude = latitude,
    longitude = longitude,
    photoUrl = photoUrl
)