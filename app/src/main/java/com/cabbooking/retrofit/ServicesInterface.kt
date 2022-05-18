package com.cabbooking.retrofit

import com.cabbooking.models.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*


interface ServicesInterface {

    companion object {
        private const val BASE_URL = "http://apg.faastr.com/api/v1/"

        const val DRIVER_REGISTRATION = "driver/register"
        const val VENDER_REGISTRATION = "vendor/register"
        const val VERIFY_OTP = "login/user"
        const val SEND_OTP = "send/otp"
        const val ALL_STATES = "states"
        const val DISTRICTS = "districts"
        const val CITY = "cities"
        const val BOOKING_LIST = "getBooking"
        const val CAB_LIST = "getCabs"
        const val DRIVER_LIST = "getDrivers"
        const val FILTERED_LIST = "getFilters"
        const val CAB_N_DRIVER_LIST = "getCabOrDriverList"
        const val CREATE_DRIVER = "createDriver"
        const val CREATE_BOOKING = "vendor/create/booking"
        const val CREATE_CAB = "create/cab"
        const val GET_PROFILE = "me"
        const val GET_PICKUP_DROP = "pickUpDrop"
        const val GET_ALL_MANUFACTURE = "getAllManufacturer"
    }

    @FormUrlEncoded
    @POST(VENDER_REGISTRATION)
    suspend fun venderRegistration(
        @Field("name") name: String? = "",
        @Field("phone") phone: String? = "",
        @Field("company_name") company_name: String? = "",
        @Field("gstin") gstin: String? = "",
    ): BaseRes<CommonResponse>

    @FormUrlEncoded
    @POST(VERIFY_OTP)
    suspend fun hitOTPApi(
        @Field("otp") otp: String? = ""
    ): BaseRes<CommonResponse>

    @FormUrlEncoded
    @POST(SEND_OTP)
    suspend fun getOtp(
        @Field("phone") phone: String? = ""
    ): BaseRes<ResponseAuthentication>


    @Multipart
    @POST(DRIVER_REGISTRATION)
    suspend fun driverRegistration(
        @Part("name") name: RequestBody? = null,
        @Part("phone") phone: RequestBody? = null,
        @Part("license_no") license_no: RequestBody? = null,
        @Part("adharcard_no") adharcard_no: RequestBody? = null,
        @Part license_image: MultipartBody.Part? = null,
        @Part adharcard_front: MultipartBody.Part? = null,
        @Part adharcard_back: MultipartBody.Part? = null,
    ): BaseRes<CommonResponse>


    @GET(ALL_STATES)
    suspend fun getAllStates(
    ): BaseRes<ArrayList<StateResponse>>


    @FormUrlEncoded
    @POST(DISTRICTS)
    suspend fun getSelectedDistrict(
        @Field("state_id") state_id: String? = ""
    ): BaseRes<ArrayList<StateResponse>>

    @FormUrlEncoded
    @POST(CITY)
    suspend fun getSelectedCity(
        @Field("districtid") districtid: String? = ""
    ): BaseRes<ArrayList<StateResponse>>


    @GET(BOOKING_LIST)
    suspend fun hitBookingApi(
        @Header("Authorization") accessToken: String? = null,
        @Query("page_no") pageNumber: Int? = null,
        @Query("count") count: Int? = null
    ): ResponseBookingList

    @GET(CAB_LIST)
    suspend fun hitCabListApi(
        @Header("Authorization") accessToken: String? = null,
        @Query("page_no") pageNumber: Int? = null,
        @Query("count") count: Int? = null
    ): CabResponse

    @GET(DRIVER_LIST)
    suspend fun hitDriverListApi(
        @Header("Authorization") accessToken: String? = null,
        @Query("page_no") pageNumber: Int? = null,
        @Query("count") count: Int? = null
    ): DriverResponse

    @GET(GET_PROFILE)
    suspend fun getProfileApi(
        @Header("Authorization") accessToken: String? = null
    ): UserPersonalResponse


    @FormUrlEncoded
    @POST(FILTERED_LIST)
    suspend fun hitFilteredBookingApi(
        @Header("Authorization") accessToken: String? = null,
        @Field("page_number") pageNumber: Int? = null,
        @Field("count") count: Int? = null,
        @Field("type[]") type: ArrayList<String>?,
        @Field("starting_day") starting_day: Long? = null,
        @Field("end_day") end_day: Long? = null,
        @Field("lastOrUpcomingDate") lastOrUpcomingDate: Long? = null
    ): ResponseBookingList

    @FormUrlEncoded
    @POST(CREATE_BOOKING)
    suspend fun createBooking(
        @Header("Authorization") accessToken: String? = null,
        @Field("enquiry_date") enquiryDate: String? = null,
        @Field("start_date") startDate: String? = null,
        @Field("end_date") endDate: String? = null,
        @Field("number_of_days") numberOfDays: Long? = null,
        @Field("pickup_location") pickupLocation: String? = null,
        @Field("drop_location") dropLocation: String? = null,
        @Field("trip_type") tripType: String? = null,
        @Field("party_name") partyName: String? = null,
        @Field("party_phone") partyPhone: String? = null,
        @Field("cab_type") cabType: String? = null,
        @Field("number_of_passenger") numberOfPassenger: String? = null,
        @Field("status") status: String? = null,
        @Field("booking_amount") bookingAmount: String? = null,
        @Field("advance_amount") advanceAmount: String? = null,
        @Field("pending_amount") pendingAmount: String? = null,
        @Field("cab_id") cabId: String? = null,
        @Field("driver_id") driverId: String? = null,
        @Field("comments") comments: String? = null,
        @Field("lead_source") lead_source: String? = null,
        @Field("lead_detail") lead_detail: String? = null
    ): ResponseBookingData

    @FormUrlEncoded
    @POST(CAB_N_DRIVER_LIST)
    suspend fun getCabNDriverList(
        @Header("Authorization") accessToken: String? = null,
        @Field("cab_list") cabList: Int? = 0,
        @Field("driver_list") driverList: Int? = 0,
    ): CabNDriverListResponse

    @GET(GET_PICKUP_DROP)
    suspend fun getPickUpDrop(
        @Header("Authorization") accessToken: String? = null,
        @Query("city_name") searchQuery: String? = null
    ): ResponseStateCity

    @GET(GET_ALL_MANUFACTURE)
    suspend fun getAllManufacturer(
        @Header("Authorization") accessToken: String? = null
    ): ResponseManufacture

    @Multipart
    @POST(CREATE_DRIVER)
    suspend fun createDriver(
        @Header("Authorization") accessToken: String? = null,
        @Part("name") name: RequestBody? = null,
        @Part("email") email: RequestBody? = null,
        @Part("phone") phone: RequestBody? = null,
        @Part("licence_number") licenseNumber: RequestBody? = null,
        @Part("is_active") isActive: RequestBody? = null,
        @Part driver_pic: MultipartBody.Part? = null,
        @Part upload_lice_pic: MultipartBody.Part? = null
    ): CreateCabNDriverResponse

    @Multipart
    @POST(CREATE_CAB)
    suspend fun createCab(
        @Header("Authorization") accessToken: String? = null,
        @Part("menufecturer_id") manufactureId: RequestBody? = null,
        @Part("vehicle_id") vehicleId: RequestBody? = null,
        @Part("registration_number") registrationNumber: RequestBody? = null,
        @Part("seating_capacity") seatingCapacity: RequestBody? = null,
        @Part("cab_type") cabType: RequestBody? = null,
        @Part("is_active") isActive: RequestBody? = null,
        @Part image: MultipartBody.Part? = null,
        @Part cab_side_pic: MultipartBody.Part? = null,
        @Part upload_cab_docs: MultipartBody.Part? = null
    ): CreateCabNDriverResponse


/*
    @FormUrlEncoded
    @POST("/accounts/login")
    suspend fun login(
        @Field("username") username: String?,
        @Field("country_code") country_code: String?,
        @Field("mobile_number") mobile_number: String?,
        @Field("email") email: String?,
        @Field("password") password: String?,
        @Field("device_type") device_type: String?,
        @Field("device_token") device_token: String?
    ): BaseRes<UserEntity>


    @FormUrlEncoded
    @POST("/accounts/registration/social")
    suspend fun socialRegistration(
        @Field("username") username: String?,
        @Field("first_name") first_name: String?,
        @Field("last_name") last_name: String?,
        @Field("social_id") social_id: String?,
        @Field("country_code") country_code: String?,
        @Field("mobile_number") mobile_number: String?,
        @Field("email") email: String?,
        @Field("device_type") device_type: String?,
        @Field("login_type") login_type: String?,
        @Field("device_token") device_token: String?
    ): BaseRes<UserEntity>

    @FormUrlEncoded
    @POST("/accounts/registration/check_social_login")
    suspend fun checkSocialLogin(
        @Field("social_id") social_id: String?,
        @Field("country_code") country_code: String?,
        @Field("mobile_number") mobile_number: String?,
        @Field("device_type") device_type: String?,
        @Field("login_type") login_type: String?,
        @Field("device_token") device_token: String?
    ): BaseRes<UserEntity>

    @FormUrlEncoded
    @POST("/accounts/register/otp/generate")
    suspend fun sendOtpMobile(
        @Field("mobile_number") mobile_number: String?,
        @Field("country_code") country_code: String?
    ): BaseRes<Any>

    @FormUrlEncoded
    @POST("/accounts/register/otp/generateon_email")
    suspend fun sendOtpEmail(
        @Field("email") email: String?
    ): BaseRes<Any>

    @FormUrlEncoded
    @POST("/accounts/otp/verify")
    suspend fun verifyOtpMobile(
        @Header("Authorization") token: String?,
        @Field("mobile_number") mobile_number: String?,
        @Field("country_code") country_code: String?,
        @Field("verification_code") verification_code: String?
    ): BaseRes<Any>

    @FormUrlEncoded
    @POST("/accounts/otp/verify_email")
    suspend fun verifyOtpEmail(
        @Header("Authorization") token: String?,
        @Field("email") email: String?,
        @Field("verification_code") verification_code: String?
    ): BaseRes<Any>

    @FormUrlEncoded
    @POST("/accounts/register/change/email/otp/generate")
    suspend fun changeEmail(
        @Field("email") email: String?,
        @Field("newemail") newEmail: String?
    ): BaseRes<String>

    @FormUrlEncoded
    @POST("/accounts/password/otp/verify")
    suspend fun verifyOtpResetPassMobile(
        @Field("mobile_number") mobile_number: String?,
        @Field("country_code") country_code: String?,
        @Field("verification_code") verification_code: String?
    ): BaseRes<Any>

    @FormUrlEncoded
    @POST("/accounts/password/otp/verify_email")
    suspend fun verifyOtpResetPassEmail(
        @Field("email") email: String?,
        @Field("verification_code") verification_code: String?
    ): BaseRes<Any>

    @FormUrlEncoded
    @POST("/accounts/password/reset")
    suspend fun resetPassword(
        @Header("Authorization") token: String?,
        @Field("new_password") new_password: String?,
        @Field("confirm_password") confirm_password: String?
    ): BaseRes<Any>

    @FormUrlEncoded
    @POST("/accounts/profile/create/basic_profile")
    suspend fun saveBasicProfile(
        @Header("Authorization") token: String?,
        @Field("gender") gender: String?,
        @Field("peoplecallme") peopleCallMe: String?,
        @Field("country_name") country_name: String?,
        @Field("i_speak") i_speak: String?,
        @Field("qualification") qualification: String?,
        @Field("profile_status") profile_status: String?,
        @Field("other_language") other_language: String?,
        @Field("other_qualification") other_qualification: String?
    ): BaseRes<UserEntity>

    @FormUrlEncoded
    @POST("/accounts/profile/update/basic")
    suspend fun updateBasicProfile(
        @Header("Authorization") token: String?,
        @Field("gender") gender: String?,
        @Field("peoplecallme") peopleCallMe: String?,
        @Field("country_name") country_name: String?,
        @Field("i_speak") i_speak: String?,
        @Field("qualification") qualification: String?,
        @Field("other_language") other_language: String?,
        @Field("other_qualification") other_qualification: String?
    ): BaseRes<UserEntity>

    @Multipart
    @POST("/accounts/profile/create/about")
    suspend fun saveAboutYourself(
        @Header("Authorization") token: String?,
        @Part("personal_status") personal_status: RequestBody?,
        @Part("bio") bio: RequestBody?,
        @Part("facebook_link") facebook_link: RequestBody?,
        @Part("instagram_link") instagram_link: RequestBody?,
        @Part("profile_status") profile_status: RequestBody?,
        @Part profileimg: MultipartBody.Part?
    ): BaseRes<UserEntity>

    @Multipart
    @POST("/accounts/profile/update/about")
    suspend fun updateAboutYourself(
        @Header("Authorization") token: String?,
        @Part("personal_status") personal_status: RequestBody?,
        @Part("bio") bio: RequestBody?,
        @Part("facebook_link") facebook_link: RequestBody?,
        @Part("instagram_link") instagram_link: RequestBody?,
        @Part("profile_status") profile_status: RequestBody?,
        @Part profileimg: MultipartBody.Part?
    ): BaseRes<UserEntity>

    @FormUrlEncoded
    @POST("/accounts/profile/create/interests")
    suspend fun saveUserInterest(
        @Header("Authorization") token: String?,
        @Field("interests") interests: String?,
        @Field("profile_status") profile_status: String?
    ): BaseRes<UserEntity>

    @FormUrlEncoded
    @POST("/accounts/profile/update/interests")
    suspend fun updateUserInterest(
        @Header("Authorization") token: String?,
        @Field("interests") interests: String?
    ): BaseRes<UserEntity>

    @FormUrlEncoded
    @POST("/accounts/profile/create/complete_profile")
    suspend fun completeUserProfile(
        @Header("Authorization") token: String?,
        @Field("avatar_url") avatar_url: String?,
        @Field("isprofile_photo") isprofile_photo: String?,
        @Field("profile_status") profile_status: String?
    ): BaseRes<UserEntity>

    @FormUrlEncoded
    @POST("/accounts/profile/update/complete_profile")
    suspend fun updateCompleteUserProfile(
        @Header("Authorization") token: String?,
        @Field("avatar_url") avatar_url: String?,
        @Field("isprofile_photo") isprofile_photo: String?
    ): BaseRes<UserEntity>

    @GET("/accounts/user/profile/{user_id}")
    suspend fun getUserProfile(
        @Header("Authorization") token: String?,
        @Path("user_id") user_id: String?
    ): BaseRes<UserEntity>

    @FormUrlEncoded
    @POST("/activities/send_friend_request")
    suspend fun sendFriendRequest(
        @Header("Authorization") token: String?,
        @Field("friend_id") friend_id: String?,
        @Field("message") message: String?
    ): BaseRes<BaseRes<String>>

    @GET("/activities/friend_request_send_receive_list")
    suspend fun getFriendSentReceiveRequests(
        @Header("Authorization") token: String?
    ): BaseRes<ArrayList<RequestUserData>>

    @FormUrlEncoded
    @POST("/activities/accept_friend_request")
    suspend fun acceptFriendRequest(
        @Header("Authorization") token: String?,
        @Field("request_id") request_id: String?
    ): BaseRes<UserEntity>

    @FormUrlEncoded
    @POST("/activities/reject_friend_request")
    suspend fun rejectFriendRequest(
        @Header("Authorization") token: String?,
        @Field("request_id") request_id: String?
    ): BaseRes<UserEntity>

    @FormUrlEncoded
    @POST("/activities/cancel_friend_request")
    suspend fun cancelFriendRequest(
        @Header("Authorization") token: String?,
        @Field("request_id") request_id: String?
    ): BaseRes<UserEntity>

    @FormUrlEncoded
    @POST("/activities/unfriend")
    suspend fun unFriend(
        @Header("Authorization") token: String?,
        @Field("request_id") request_id: String?
    ): BaseRes<UserEntity>

    @GET("/activities/friend_list")
    suspend fun getMyFriendsList(
        @Header("Authorization") token: String?
    ): BaseRes<ArrayList<FriendData>>

    @GET("/events/event_category_list")
    suspend fun getEventsCategory(
        @Header("Authorization") token: String?
    ): BaseRes<ArrayList<EventCategory>>

    @GET("/events/event_list/{category_id}")
    suspend fun getEvents(
        @Header("Authorization") token: String?,
        @Path("category_id") category_id: Int?
    ): BaseRes<ArrayList<EventData>>

    @FormUrlEncoded
    @POST("/events/show_interest")
    suspend fun showInterestMap(
        @Header("Authorization") token: String?,
        @FieldMap map: HashMap<String, Any?>
    ): BaseRes<EventData>

    @POST("/events/remove_interest/{event_id}")
    suspend fun removeInterest(
        @Header("Authorization") token: String?,
        @Path("event_id") event_id: Int?
    ): BaseRes<EventData>

    @GET("/events/show_interest_list")
    suspend fun getMyInterestEvents(
        @Header("Authorization") token: String?
    ): BaseRes<ArrayList<EventData>>

    @GET("/business/business_category_list")
    suspend fun getPlacesCategory(
        @Header("Authorization") token: String?
    ): BaseRes<ArrayList<EventCategory>>

    @FormUrlEncoded
    @POST("/business/filtered_business_list")
    suspend fun getPlaces(
        @Header("Authorization") token: String?,
        @FieldMap map: HashMap<String, Any?>
    ): BaseRes<ArrayList<BusinessData>>

    @FormUrlEncoded
    @POST("/business/filtered_business_list_guest")
    suspend fun getGuestPlaces(
        @FieldMap map: HashMap<String, Any?>
    ): BaseRes<ArrayList<BusinessData>>

    @FormUrlEncoded
    @POST("/events/nearby_event_list")
    suspend fun getNearByEvents(
        @Header("Authorization") token: String?,
        @FieldMap map: HashMap<String, Any?>
    ): BaseRes<ArrayList<EventData>>

    @FormUrlEncoded
    @POST("/business/nearby_business_list")
    suspend fun getNearByPlaces(
        @Header("Authorization") token: String?,
        @FieldMap map: HashMap<String, Any?>
    ): BaseRes<ArrayList<BusinessData>>

    @GET("https://maps.googleapis.com/maps/api/place/autocomplete/json")
    suspend fun getLocation(
        @Query("key") key: String?,
        @Query("input") input: String?
    ): SearchLocationResponse

    @GET("https://maps.googleapis.com/maps/api/place/details/json")
    suspend fun getPlaceDetails(
        @Query("key") key: String?,
        @Query("placeid") placeid: String?
    ): PlaceDetailsResponse

    @FormUrlEncoded
    @POST("/events/search_event")
    suspend fun searchEvents(
        @Header("Authorization") token: String?,
        @Field("event_name") event_name: String?
    ): BaseRes<ArrayList<EventData>>

    @FormUrlEncoded
    @POST("/business/search_business")
    suspend fun searchPlaces(
        @Header("Authorization") token: String?,
        @Field("business_name") business_name: String?
    ): BaseRes<ArrayList<BusinessData>>

    @FormUrlEncoded
    @POST("/sidebar/user/change_profile_visibility")
    suspend fun changeProfileVisibility(
        @Header("Authorization") token: String?,
        @FieldMap map: HashMap<String, Any?>
    ): BaseRes<String>

    @Multipart
    @POST("/api/v1/chat/upload_media")
    suspend fun uploadChatMedia(
        @Header("Authorization") token: String?,
        @Part video: MultipartBody.Part?,
        @Part thumb: MultipartBody.Part?,
        @Part audio: MultipartBody.Part?,
        @Part image: ArrayList<MultipartBody.Part?>,
        @Part docs: MultipartBody.Part?
    ): BaseRes<MediaData>

    @GET("/accounts/qualification_list")
    suspend fun getQualificationList(
        @Header("Authorization") token: String?
    ): BaseRes<ArrayList<ListData>>

    @GET("/accounts/country_list")
    suspend fun getCountryList(
        @Header("Authorization") token: String?
    ): BaseRes<ArrayList<ListData>>

    @GET("/accounts/interest_list")
    suspend fun getInterestList(
        @Header("Authorization") token: String?
    ): BaseRes<ArrayList<ListData>>

    @Multipart
    @POST("/api/v1/chat/group/create")
    suspend fun createGroup(
        @Header("Authorization") token: String?,
        @Part("group_name") group_name: RequestBody?,
        @Part group_image: MultipartBody.Part?
    ): BaseRes<GroupData>

    @FormUrlEncoded
    @PUT("/accounts/profile/update/fullname")
    suspend fun updateFullName(
        @Header("Authorization") token: String?,
        @FieldMap map: HashMap<String, String>
    ): BaseRes<String>

    @FormUrlEncoded
    @PUT("/accounts/password/change")
    suspend fun changePassword(
        @Header("Authorization") token: String?,
        @FieldMap map: HashMap<String, String>
    ): BaseRes<String>

    @FormUrlEncoded
    @POST("/accounts/update/otp/mobile")
    suspend fun sendOtpForUpdateMobile(
        @Header("Authorization") token: String?,
        @FieldMap map: HashMap<String, String>
    ): BaseRes<String>

    @FormUrlEncoded
    @POST("/accounts/update/otp/email")
    suspend fun sendOtpForUpdateEmail(
        @Header("Authorization") token: String?,
        @FieldMap map: HashMap<String, String>
    ): BaseRes<String>

    @FormUrlEncoded
    @POST("/accounts/update/otp/verify/mobile")
    suspend fun verifyOtpForUpdateMobile(
        @Header("Authorization") token: String?,
        @FieldMap map: HashMap<String, String?>
    ): BaseRes<String>

    @FormUrlEncoded
    @POST("/accounts/update/otp/verify/email")
    suspend fun verifyOtpForUpdateEmail(
        @Header("Authorization") token: String?,
        @FieldMap map: HashMap<String, String?>
    ): BaseRes<String>

    @FormUrlEncoded
    @PUT("/accounts/profile/update/mobile")
    suspend fun updateMobileNumber(
        @Header("Authorization") token: String?,
        @FieldMap map: HashMap<String, String?>
    ): BaseRes<String>

    @FormUrlEncoded
    @PUT("/accounts/profile/update/email")
    suspend fun updateEmailId(
        @Header("Authorization") token: String?,
        @FieldMap map: HashMap<String, String?>
    ): BaseRes<String>

    @FormUrlEncoded
    @POST("/activities/friend/block")
    suspend fun blockFriend(
        @Header("Authorization") token: String?,
        @Field("friend_id") friend_id: String?
    ): BaseRes<String>

    @FormUrlEncoded
    @POST("/activities/friend/unblock")
    suspend fun unblockFriend(
        @Header("Authorization") token: String?,
        @Field("friend_id") friend_id: String?
    ): BaseRes<Any>

    @GET("/activities/blocked_list")
    suspend fun getBlockedUserList(
        @Header("Authorization") token: String?
    ): BaseRes<ArrayList<BlockedUserData>>

    @FormUrlEncoded
    @POST("/events/invite_friends")
    suspend fun inviteFriendToEvent(
        @Header("Authorization") token: String?,
        @FieldMap map: HashMap<String, Any?>
    ): BaseRes<InvitationData>

    @FormUrlEncoded
    @POST("/events/accept_invitation")
    suspend fun acceptInvitation(
        @Header("Authorization") token: String?,
        @FieldMap map: HashMap<String, Any?>
    ): BaseRes<Any>

    @FormUrlEncoded
    @POST("/events/reject_invitation")
    suspend fun rejectInvitation(
        @Header("Authorization") token: String?,
        @FieldMap map: HashMap<String, Any?>
    ): BaseRes<Any>

    @GET("/events/sent_evnet_invitation_list")
    suspend fun getSentInvitationList(
        @Header("Authorization") token: String?
    ): BaseRes<ArrayList<InvitationData>>

    @GET("/events/recieved_evnet_invitation_list")
    suspend fun getReceivedInvitationList(
        @Header("Authorization") token: String?
    ): BaseRes<ArrayList<InvitationData>>

    @FormUrlEncoded
    @POST("/activities/home_search")
    suspend fun globalSearch(
        @Header("Authorization") token: String?,
        @Field("query") query: String?
    ): BaseRes<SearchResult>

    @FormUrlEncoded
    @POST("/api/v1/chat/send_multiple_notification")
    suspend fun sendNotification(
        @Header("Authorization") token: String?,
        @Field("ids") ids: String?,
        @Field("title") title: String?,
        @Field("message") message: String?,
        @Field("notification_data") notification_data: String?,
        @Field("notification_type") notification_type: String?
    ): BaseRes<Notification>

    @GET("/accounts/language_list")
    suspend fun getLanguageList(
        @Header("Authorization") token: String?
    ): BaseRes<ArrayList<ListData>>

    @FormUrlEncoded
    @POST("/sidebar/checkin_business")
    suspend fun checkIn(
        @Header("Authorization") token: String?,
        @Field("business_id") business_id: String?,
        @Field("business_category_id") business_category_id: String?,
        @Field("friend_ids") friend_ids: String?
    ): BaseRes<Any>

    @GET("/sidebar/checkin_business_list")
    suspend fun getCheckInList(
        @Header("Authorization") token: String?
    ): BaseRes<ArrayList<CheckInData>>

    @GET("/activities/notification_list")
    suspend fun getNotificationsList(
        @Header("Authorization") token: String?
    ): BaseRes<ArrayList<Notification>>

*/

}