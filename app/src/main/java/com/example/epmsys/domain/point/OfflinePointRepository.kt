package com.example.epmsys.domain.point

import com.example.epmsys.data.point.PointEntity
import kotlinx.coroutines.flow.Flow

interface OfflinePointRepository {

    suspend fun AddPoint(point: PointEntity): Unit

    fun getAllPoints(): Flow<List<PointEntity>>
}