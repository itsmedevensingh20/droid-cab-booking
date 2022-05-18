package com.cabbooking.models


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ResponseBookingList(
    @SerializedName("Booking_list") val bookingList: ArrayList<ResponseBooking>? = null,
    @SerializedName("data") val bookingDataList: ArrayList<ResponseBooking>? = null,
    @SerializedName("error") val error: String? = null, // null
    @SerializedName("message") val message: String? = null, // List sucessfully
    @SerializedName("statusCode") val statusCode: Int? = null, // 200
    @SerializedName("status") val status: Int? = null// 200

)
@Keep
data class ResponseBookingData(
    @SerializedName("data") val bookingData: ResponseBooking? = null,
    @SerializedName("error") val error: String? = null, // null
    @SerializedName("message") val message: String? = null, // List sucessfully
    @SerializedName("statusCode") val statusCode: Int? = null, // 200
    @SerializedName("status") val status: Int? = null// 200

)

@Keep
data class ResponseBooking(
    @SerializedName("id") val id: Int? = null, // 5500
    @SerializedName("advance_amount") val advanceAmount: Double? = null, // 500.00
    @SerializedName("booking_amount") val bookingAmount: Double? = null, // 6000.00
    @SerializedName("driversName") val driverName: String? = null, // null
    @SerializedName("driver_number") val driverNumber: String? = null, // null
    @SerializedName("drop_location") val dropLocation: String? = null, // New Delhi / Delhi
    @SerializedName("license_no") val licenseNo: String? = null, // null
    @SerializedName("party_name") val partyName: String? = null, // Mriganka
    @SerializedName("pendingAmount") val pendingAmount: Double? = null, // 5500
    @SerializedName("pickup_location") val pickupLocation: String? = null, // Rudrapur / Uttarakhand
    @SerializedName("start_date") val startDate: String? = null, // 1641688200
    @SerializedName("trip_type") val tripType: String? = null // Round Trip
)


@Keep
data class ResponseFilterBody(
    val startingDay: Long? = null,
    val  endDay: Long? = null,
    val typeOfFilter: ArrayList<String> = ArrayList(),
    val lastOrUpcomingDate: Long? = null
)
