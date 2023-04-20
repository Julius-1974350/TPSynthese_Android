package ca.qc.cstj.tenretni.core

sealed class ApiResult<out R> {
    data class Success<out R>(val data: R) : ApiResult<R>()
    data class Error(val exception: Exception): ApiResult<Nothing>()
    object Loading: ApiResult<Nothing>()
}