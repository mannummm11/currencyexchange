package com.achrya.paypaychallenge.utils

sealed class NetworkResult<T>(val status: ApiStatus, val data: T?=null, val message: String?=null) {

    data class Success<T>(val _data: T?) : NetworkResult<T>(status = ApiStatus.SUCCESS, data = _data, message = null)
    data class Error<T>(val exception: String) : NetworkResult<T>(status = ApiStatus.ERROR, message = exception)
    data class Loading<T>(val isLoading: Boolean) : NetworkResult<T>(status = ApiStatus.LOADING)

}

enum class ApiStatus {
    SUCCESS,
    ERROR,
    LOADING
}
