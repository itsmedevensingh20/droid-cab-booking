package com.cabbooking.models


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class CreateCabNDriverResponse(
    @SerializedName("data") val data: Data? = null,
    @SerializedName("message") val message: String? = null, // Cab successfully added
    @SerializedName("status") val status: Int? // 200
) {
    @Keep
    data class Data(
        @SerializedName("cab_side_pic") val cabSidePic: String? = null, // media/230322_13_46_46_10__cab.jpg
        @SerializedName("cab_type") val cabType: String? = null, // Hatchback
        @SerializedName("company_id") val companyId: String? = null, // 42
        @SerializedName("created_at") val createdAt: String? = null, // 2022-03-23T08:16:46.000000Z
        @SerializedName("id") val id: Int? = 0, // 33
        @SerializedName("image") val image: String? = null, // media/230322_13_46_46_10__cab.jpg
        @SerializedName("is_active") val isActive: String? = null, // Active
        @SerializedName("menufecturer_id") val menufecturerId: String? = null, // 5
        @SerializedName("registration_number") val registrationNumber: String? = null, // Ufufufugfuf
        @SerializedName("seating_capacity") val seatingCapacity: String? = null, // 4
        @SerializedName("updated_at") val updatedAt: String? = null, // 2022-03-23T08:16:46.000000Z
        @SerializedName("upload_cab_docs") val uploadCabDocs: String? = null, // media/230322_13_46_46_10__cab.jpg
        @SerializedName("vehicle_id") val vehicleId: String? = null // 10
    )
}