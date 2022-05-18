package com.cabbooking.models


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ResponseAuthentication(
    @SerializedName("data")
    var `data`: Data?,
    @SerializedName("message")
    var message: String? = null, // OTP sent successfully on your number
    @SerializedName("status")
    var status: Int? = 0 // 200
)

@Keep
data class Data(
    @SerializedName("msg")
    var msg: String? = null, // OTP sent successfully on your number
    @SerializedName("verified")
    var verified: String? = null // yes
)
