package com.example.epmsys.domain.location

import android.location.Location

interface LocationTrackerRepository {
    suspend fun getCurrentLocation(): Location?
}