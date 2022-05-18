package com.cabbooking.models


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ResponseStateCity(
    @SerializedName("data") val data: ArrayList<ResponseStateCityList> = ArrayList(),
    @SerializedName("message") val message: String? = null, // Cities Data
    @SerializedName("status") val status: Int? = null // 200
)

@Keep
data class ResponseStateCityList(
    @SerializedName("city_name") val cityName: String? = null, // Haladia
    @SerializedName("state_title") val stateTitle: String? = null // Maharashtra
)
