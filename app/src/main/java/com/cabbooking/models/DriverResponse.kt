package com.cabbooking.models


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class DriverResponse(
    @SerializedName("data") val data: List<DriverData>? = null,
    @SerializedName("message") val message: String? = null, // Drivers list
    @SerializedName("status") val status: Int? = null // 200
)
@Keep
data class DriverData(
    @SerializedName("avatar") val avatar: String? = null, // no-image.jpg
    @SerializedName("driver_request") val driverRequest: String? = null, // Approved
    @SerializedName("is_active") val isActive: String? = null, // yes
    @SerializedName("license_no") val licenseNo: String? = null, // ujdud9090909
    @SerializedName("name") val name: String? = null, // XYZ Singh
    @SerializedName("phone") val phone: String? = null // 9189898989
)
