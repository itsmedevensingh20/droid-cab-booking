package com.cabbooking.retrofit

import com.google.gson.Gson
import retrofit2.HttpException
import java.net.SocketTimeoutException

open class ResponseHandler {

    object ApiErrorHandler {
        fun handleException(exception: Exception): String {
            exception.printStackTrace()
            return when (exception) {
                is HttpException -> handleHttpException(exception)
                is SocketTimeoutException -> "Socket Timeout Exception"
                else -> "Something went wrong"
            }
        }

        private fun handleHttpException(exception: HttpException): String {
            return try {
                val reader = exception.response()?.errorBody()?.charStream()
                val errorBody = Gson().fromJson(reader, ResponseHandler.ErrorBean::class.java)
                errorBody.message
            } catch (e: Exception) {
                "Something went wrong"
            }
        }
    }

    fun <T : Any> handleSuccess(data: T): Resource<T> {
        return Resource.success(data)
    }

    fun <T : Any> handleException(e: Exception): Resource<T> {
        return when (e) {
            is HttpException -> Resource.error(handleHttpException(e), null)
            is SocketTimeoutException -> Resource.error(
                getErrorMessage(ErrorCodes.SocketTimeOut.code),
                null
            )
            else -> Resource.error(getErrorMessage(Int.MAX_VALUE), null)
        }
    }

    private fun handleHttpException(exception: HttpException): String {
        return try {
            val reader = exception.response()?.errorBody()?.charStream()
            val errorBody = Gson().fromJson(reader, ErrorBean::class.java)
            errorBody.message
        } catch (e: Exception) {
            "Something went wrong"
        }
    }

    private fun getErrorMessage(code: Int): String {
        return when (code) {
            ErrorCodes.SocketTimeOut.code -> "Timeout"
            401 -> "Unauthorised Access"
            404 -> "Not found"
            else -> "Something went wrong"
        }
    }

    data class ErrorBean(
        val message: String
    )
}