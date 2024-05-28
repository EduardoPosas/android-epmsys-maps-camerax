package com.example.epmsys.data.point

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PointDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun AddPoint(point: PointEntity)

    @Query("Select * from point")
    fun getAllPoints(): Flow<List<PointEntity>>

}