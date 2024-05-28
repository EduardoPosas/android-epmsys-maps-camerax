package com.example.epmsys.data.point

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PointEntity::class], version = 1, exportSchema = false)
abstract class PointDatabase : RoomDatabase() {
    abstract fun pointDao(): PointDao
}