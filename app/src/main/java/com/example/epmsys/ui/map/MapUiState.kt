package com.example.epmsys.ui.map

import com.example.epmsys.ui.point.PointDetails

data class MapUiState (
    val pointList: List<PointDetails> = emptyList()
)