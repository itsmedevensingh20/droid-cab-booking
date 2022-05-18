package com.cabbooking.models


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ResponseManufacture(
    @SerializedName("data") val `data`: List<ManufactureData>? = null,
    @SerializedName("message") val message: String? = null, // Cabs Data
    @SerializedName("status") val status: Int? = null // 200
)

@Keep
data class ManufactureData(
    @SerializedName("manufecturere_id") val manufactureId: String? = null, // 1
    @SerializedName("menufecturers_list") val manufactureList: ArrayList<Manufacture> = ArrayList(),
    @SerializedName("menufecturers_name") val manufactureName: String? = null // Honda
)

@Keep
data class Manufacture(
    @SerializedName("created_at") val createdAt: String? = null, // 2021-02-20 17:48:52
    @SerializedName("id") val id: String? = null, // 1
    @SerializedName("menufecturer_id") val manufactureId: String? = null, // 1
    @SerializedName("seats") val seats: String? = null, // 4
    @SerializedName("title") val title: String? = null, // Amaze
    @SerializedName("type") val type: String? = null, // Sedan
    @SerializedName("updated_at") val updatedAt: String? = null, // 2021-02-20 17:49:31
    @SerializedName("vihicle_request") val vehicleRequest: String? = null // null
)
    
