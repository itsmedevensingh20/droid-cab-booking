package com.cabbooking.models


import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Keep
@Entity(tableName = "UserTable")
data class CommonResponse(
    @PrimaryKey(autoGenerate = true)
    var _id: Int? = 0,
    @SerializedName("id")
    var userId: Int? = 0,
    @SerializedName("adharcard_back")
    var adharcardBack: String? = null, // media/100721_10_03_38_46_adhar_back.jpg
    @SerializedName("adharcard_front")
    var adharcardFront: String? = null, // media/100721_10_03_38_46_adhar_front.jpg
    @SerializedName("adharcard_no")
    var adharCardNo: String? = null, // 1234567890
    @SerializedName("license_image")
    var licenseImage: String? = null, // media/100721_10_03_38_46_license.jpg
    @SerializedName("license_no")
    var licenseNo: String? = null, // UP11 2222
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("email")
    var email: String? = null,
    @SerializedName("msg")
    var msg: String? = null,
    @SerializedName("pincode")
    var pinCode: String? = null,
    @SerializedName("otp")
    var otp: String? = null,
    @SerializedName("verified")
    var verified: String? = null,
    @SerializedName("state")
    var state: String? = null,
    @SerializedName("role")
    var role: String? = null,
//    @SerializedName("is_active")
//    var is_active: String? = null,
    @SerializedName("district")
    var district: String? = null,
    @SerializedName("city")
    var city: String? = null,
    @SerializedName("company_name")
    var companyName: String? = null,
    @SerializedName("gstin")
    var gstNumber: String? = null,
    @SerializedName("userToken")
    var userToken: String? = null,
    @SerializedName("phone")
    var phone: String? = null,
    @SerializedName("isLogIn")
    var isLogIn: Boolean? = null,
)