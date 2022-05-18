package com.cabbooking.models


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class UserPersonalResponse(
    @SerializedName("data") val data: Data? = null,
    @SerializedName("message") val message: String? = null, // User Data
    @SerializedName("status") val status: Int? = null // 200
) {
    @Keep
    data class Data(
        @SerializedName("avatar") val avatar: String? = null, // no-image.jpg
        @SerializedName("balance") val balance: Long? = null, // 10000
        @SerializedName("company_id") val companyId: String? = null, // 42
        @SerializedName("created_at") val createdAt: String? = null, // 2021-12-04T11:23:20.000000Z
        @SerializedName("deleted_at") val deletedAt: String? = null, // null
        @SerializedName("email") val email: String? = null, // 1234567899@faastr.com
        @SerializedName("email_verified_at") val emailVerifiedAt: String? = null, // null
        @SerializedName("id") val id: Int? = null, // 72
        @SerializedName("is_active") val isActive: String? = null, // yes
        @SerializedName("name") val name: String? = null, // Demo
        @SerializedName("phone") val phone: String? = null, // 1234567899
        @SerializedName("role") val role: String? = null, // vendor_admin
        @SerializedName("updated_at") val updatedAt: String? = null, // 2022-01-11T11:56:03.000000Z
        @SerializedName("verified") val verified: String? = null // yes
    )
}