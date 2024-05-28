package com.example.epmsys.data.point

import com.example.epmsys.domain.point.OfflinePointRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OfflinePointRepositoryImpl @Inject constructor(
    private val pointDatabase: PointDatabase
) : OfflinePointRepository {
    override suspend fun AddPoint(point: PointEntity) = pointDatabase.pointDao().AddPoint(point)

    override fun getAllPoints(): Flow<List<PointEntity>> = pointDatabase.pointDao().getAllPoints()
}