package com.cabbooking.models


import com.google.gson.annotations.SerializedName

data class StateResponse(
    @SerializedName("state_id")
    var stateId: String?,
    @SerializedName("name")
    var cityName: String?,
    @SerializedName("districtid")
    var districtId: String?,
    @SerializedName("district_title")
    var districtName: String?,
    @SerializedName("state_title")
    var stateTitle: String?
)