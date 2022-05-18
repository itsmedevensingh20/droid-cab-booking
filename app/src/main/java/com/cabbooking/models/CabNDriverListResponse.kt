package com.cabbooking.models


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class CabNDriverListResponse(
    @SerializedName("data") val data: ArrayList<CabNDriverListResponseData>? = null,
    @SerializedName("message") val message: String? = null, // Cabs list
    @SerializedName("status") val status: Int? = null // 200
)

@Keep
data class CabNDriverListResponseData(
    @SerializedName("cab_pic") val cabPic: String? = null, // default-car.jpg
    @SerializedName("cab_side_pic") val cabSidePic: String? = null, // null
    @SerializedName("cab_type") val cabType: String? = null, // Sedan
    @SerializedName("company_id") val companyId: String? = null, // 42
    @SerializedName("driver_name") val driverName: String? = null, // 42
    @SerializedName("created_at") val createdAt: String? = null, // 2022-01-11T11:14:11.000000Z
    @SerializedName("deleted_at") val deletedAt: String? = null, // null
    @SerializedName("driver_lice_pic") val driverLicePic: String? = null, // null
    @SerializedName("driver_request") val driverRequest: String? = null, // null
    @SerializedName("id") val id: Int? = null, // 28
    @SerializedName("image") val image: String? = null, // null
    @SerializedName("is_active") val isActive: String? = null, // yes
    @SerializedName("license_no") val licenseNo: String? = null, // PB08098098043454
    @SerializedName("menufecturer_id") val manufactureId: String? = null, // 4
    @SerializedName("registration_number") val registrationNumber: String? = null, // UK06TA2777
    @SerializedName("seating_capacity") val seatingCapacity: String? = null, // 4
    @SerializedName("updated_at") val updatedAt: String? = null, // 2022-01-11T11:14:11.000000Z
    @SerializedName("upload_cab_docs") val uploadCabDocs: Any? = null, // null
    @SerializedName("user_id") val userId: String? = null, // 80
    @SerializedName("vehicle_id") val vehicleId: String? = null // 8
)
