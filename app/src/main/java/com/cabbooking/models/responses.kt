package com.cabbooking.models


data class BaseRes<T>(
    val message: String? = null,
    val error: String? = null,
    val status: Int? = null,
    val statusCode: Int? = null,
    val data: T? = null
)

