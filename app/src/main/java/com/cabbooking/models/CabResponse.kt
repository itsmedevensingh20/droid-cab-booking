package com.cabbooking.models


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class CabResponse(
    @SerializedName("data") val data: ArrayList<CabData>? = null,
    @SerializedName("message") val message: String? = null, // Cabs list
    @SerializedName("status") val status: Int? = null // 200
)

@Keep
data class CabData(
    @SerializedName("cab_pic") val cabPic: String? = null, // default-car.jpg
    @SerializedName("is_active") val isActive: String? = null, // yes
    @SerializedName("rating") val rating: String? = null, // 4+
    @SerializedName("registration_number") val registrationNumber: String? = null, // UK06TA2777
    @SerializedName("seats") val seats: String? = null, // 4
    @SerializedName("title") val title: String? = null, // Zest
    @SerializedName("type") val type: String? = null, // Sedan
    @SerializedName("avatar") val avatar: String? = null, // Sedan
    @SerializedName("vihicle_request") val vehicleRequest: String? = null // Approved
)
