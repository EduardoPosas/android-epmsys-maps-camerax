package com.example.epmsys.data.point

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.epmsys.ui.point.PointDetails

@Entity(tableName = "point")
data class PointEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val category: String,
    val action: String,
    @ColumnInfo("date_time")
    val dateTime: String,
    val latitude: Double,
    val longitude: Double,
    @ColumnInfo("photo_url")
    val photoUrl: String
)


fun PointEntity.toPointDetails() = PointDetails(
    category = category,
    action = action,
    dateTime = dateTime,
    latitude = latitude,
    longitude = longitude,
    photoUrl = photoUrl
)