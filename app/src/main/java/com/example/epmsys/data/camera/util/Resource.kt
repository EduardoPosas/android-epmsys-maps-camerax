package com.example.epmsys.data.camera.util

sealed class Resource<T> {
    data class Success<T>(val uri: T) : Resource<T>()
    data class Error<T>(val message: String) : Resource<T>()
}