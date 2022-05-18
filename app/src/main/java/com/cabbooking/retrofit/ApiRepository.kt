package com.cabbooking.retrofit

import com.cabbooking.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class ApiRepository @Inject constructor(
    private val servicesInterface: ServicesInterface
) {

    companion object {
        private val responseHandler = ResponseHandler()

        fun getRequestBody(value: String?): RequestBody? {
            if (value == null)
                return null
            val contentType = "text/plain".toMediaTypeOrNull()
            return value.toRequestBody(contentType)
        }

        private fun getMultipartBody(filePath: String?, keyName: String): MultipartBody.Part? {
            var multiPartBody: MultipartBody.Part? = null

            if (!filePath.isNullOrEmpty()) {
                val file = File(filePath)
                if (file.exists()) {
                    val requestBodyProfileImage = file.asRequestBody("*/*".toMediaTypeOrNull())
                    multiPartBody =
                        MultipartBody.Part.createFormData(
                            keyName,
                            file.name,
                            requestBodyProfileImage
                        )
                }
            }

            return multiPartBody
        }
    }
    //    val listData: Flow<PagingData<ResponseBooking>> = Pager(PagingConfig(pageSize = 10)) {
//        BookingPagingSource(servicesInterface, accessToken)
//    }.flow.cachedIn(viewModelScope)

//    fun getBookingData(accessToken: String? = null): Flow<PagingData<ResponseBooking>> {
//        return Pager(PagingConfig(pageSize = 10)) {
//            BookingPagingSource(servicesInterface, accessToken)
//        }.flow
//    }


    /**---Api Calling---**/
    suspend fun hitCreateVenderAccountApi(
        userData: CommonResponse,
        p0: (BaseRes<CommonResponse>) -> Unit,
        p1: (Exception) -> Unit
    ) {
        try {
            val response = servicesInterface.venderRegistration(
                name = userData.name,
                phone = userData.phone,
                company_name = userData.companyName,
                gstin = userData.gstNumber
            )
            withContext(Dispatchers.Main) { p0(response) }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) { p1(e) }
        }
    }


    suspend fun driverRegistration(
        commonResponse: CommonResponse,
        license_image: MultipartBody.Part?,
        adharcard_front: MultipartBody.Part?,
        adharcard_back: MultipartBody.Part?,
        p0: (BaseRes<CommonResponse>) -> Unit,
        p1: (Exception) -> Unit
    ) {
        try {
            val response = servicesInterface.driverRegistration(
                name = getRequestBody(commonResponse.name),
                phone = getRequestBody(commonResponse.phone),
                license_no = getRequestBody(commonResponse.licenseNo),
                adharcard_no = getRequestBody(commonResponse.adharCardNo),
                license_image = license_image,
                adharcard_front = adharcard_front,
                adharcard_back = adharcard_back
            )
            withContext(Dispatchers.Main) { p0(response) }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) { p1(e) }
        }
    }

    suspend fun hitOTPApi(
        userData: CommonResponse,
        p0: (BaseRes<CommonResponse>) -> Unit,
        p1: (Exception) -> Unit
    ) {
        try {
            val response = servicesInterface.hitOTPApi(
                otp = userData.otp
            )
            withContext(Dispatchers.Main) { p0(response) }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) { p1(e) }
        }
    }

    suspend fun getOtp(
        userData: CommonResponse,
        p0: (BaseRes<ResponseAuthentication>) -> Unit,
        p1: (Exception) -> Unit
    ) {
        try {
            val response = servicesInterface.getOtp(
                phone = userData.phone
            )
            withContext(Dispatchers.Main) { p0(response) }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) { p1(e) }
        }
    }

    suspend fun getAllStates(
        p0: (BaseRes<ArrayList<StateResponse>>) -> Unit,
        p1: (Exception) -> Unit
    ) {
        try {
            val response = servicesInterface.getAllStates(
            )
            withContext(Dispatchers.Main) { p0(response) }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) { p1(e) }
        }
    }

    suspend fun getSelectedDistrict(
        state_id: String,
        p0: (BaseRes<ArrayList<StateResponse>>) -> Unit,
        p1: (Exception) -> Unit
    ) {
        try {
            val response = servicesInterface.getSelectedDistrict(
                state_id = state_id
            )
            withContext(Dispatchers.Main) { p0(response) }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) { p1(e) }
        }
    }

    suspend fun getSelectedCity(
        districtId: String,
        p0: (BaseRes<ArrayList<StateResponse>>) -> Unit,
        p1: (Exception) -> Unit
    ) {
        try {
            val response = servicesInterface.getSelectedCity(
                districtid = districtId
            )
            withContext(Dispatchers.Main) { p0(response) }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) { p1(e) }
        }
    }

    suspend fun hitBookingApi(
        accessToken: String?,
        page_no: Int? = null,
        count: Int? = null,
        p0: (ResponseBookingList) -> Unit,
        p1: (Exception) -> Unit
    ) {
        try {
            val response = servicesInterface.hitBookingApi(
                accessToken = accessToken,
                pageNumber = page_no,
                count = count
            )
            withContext(Dispatchers.Main) { p0(response) }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) { p1(e) }
        }
    }

    suspend fun createBooking(
        accessToken: String? = null,
        enquiryDate: String? = null,
        startDate: String? = null,
        endDate: String? = null,
        numberOfDays: Long? = null,
        pickupLocation: String? = null,
        dropLocation: String? = null,
        tripType: String? = null,
        partyName: String? = null,
        partyPhone: String? = null,
        cabType: String? = null,
        numberOfPassenger: String? = null,
        status: String? = null,
        bookingAmount: String? = null,
        advanceAmount: String? = null,
        pendingAmount: String? = null,
        cabId: String? = null,
        driverId: String? = null,
        comments: String? = null,
        lead_source: String? = null,
        lead_detail: String? = null,
        p0: (ResponseBookingData) -> Unit,
        p1: (Exception) -> Unit
    ) {
        try {
            val response = servicesInterface.createBooking(
                accessToken = accessToken,
                enquiryDate = enquiryDate,
                startDate = startDate,
                endDate = endDate,
                numberOfDays = numberOfDays,
                pickupLocation = pickupLocation,
                dropLocation = dropLocation,
                tripType = tripType,
                partyName = partyName,
                partyPhone = partyPhone,
                cabType = cabType,
                numberOfPassenger = numberOfPassenger,
                status = status,
                bookingAmount = bookingAmount,
                advanceAmount = advanceAmount,
                pendingAmount = pendingAmount,
                cabId = cabId,
                driverId = driverId,
                comments = comments,
                lead_source = lead_source,
                lead_detail = lead_detail
            )
            withContext(Dispatchers.Main) { p0(response) }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) { p1(e) }
        }
    }

    suspend fun hitCabListApi(
        accessToken: String?,
        page_no: Int? = null,
        count: Int? = null,
        p0: (CabResponse) -> Unit,
        p1: (Exception) -> Unit
    ) {
        try {
            val response = servicesInterface.hitCabListApi(
                accessToken = accessToken,
                pageNumber = page_no,
                count = count
            )
            withContext(Dispatchers.Main) { p0(response) }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) { p1(e) }
        }
    }

    suspend fun getCabNDriverList(
        accessToken: String?,
        cabList: Int? = null,
        driverList: Int? = null,
        p0: (CabNDriverListResponse) -> Unit,
        p1: (Exception) -> Unit
    ) {
        try {
            val response = servicesInterface.getCabNDriverList(
                accessToken = accessToken,
                cabList = cabList,
                driverList = driverList
            )
            withContext(Dispatchers.Main) { p0(response) }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) { p1(e) }
        }
    }

    suspend fun getAllManufacturer(
        accessToken: String?,
        p0: (ResponseManufacture) -> Unit,
        p1: (Exception) -> Unit
    ) {
        try {
            val response = servicesInterface.getAllManufacturer(
                accessToken = accessToken
            )
            withContext(Dispatchers.Main) { p0(response) }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) { p1(e) }
        }
    }

    suspend fun getPickUpDrop(
        accessToken: String?,
        searchQuery: String?,
        p0: (ResponseStateCity) -> Unit,
        p1: (Exception) -> Unit
    ) {
        try {
            val response = servicesInterface.getPickUpDrop(
                accessToken = accessToken,
                searchQuery = searchQuery
            )
            withContext(Dispatchers.Main) { p0(response) }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) { p1(e) }
        }
    }

    suspend fun createDriver(
        accessToken: String?,
        name: String? = null,
        phone: String? = null,
        email: String? = null,
        licenseNumber: String? = null,
        isActive: String? = null,
        driver_pic: MultipartBody.Part?,
        upload_lice_pic: MultipartBody.Part?,
        p0: (CreateCabNDriverResponse) -> Unit,
        p1: (Exception) -> Unit
    ) {
        try {
            val response = servicesInterface.createDriver(
                accessToken = accessToken,
                name = getRequestBody(name),
                email = getRequestBody(email),
                phone = getRequestBody(phone),
                licenseNumber = getRequestBody(licenseNumber),
                isActive = getRequestBody(isActive),
                driver_pic = driver_pic,
                upload_lice_pic = upload_lice_pic
            )
            withContext(Dispatchers.Main) { p0(response) }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) { p1(e) }
        }
    }

    suspend fun createCab(
        accessToken: String?,
        manufacture: String? = null,
        vehicle: String? = null,
        seatingCapacity: String? = null,
        cabType: String? = null,
        regNumber: String? = null,
        isActive: String? = null,
        uploadCabFrontImg: MultipartBody.Part?,
        uploadCabSideImg: MultipartBody.Part?,
        uploadCabDocsImg: MultipartBody.Part?,
        p0: (CreateCabNDriverResponse) -> Unit,
        p1: (Exception) -> Unit
    ) {
        try {
            val response = servicesInterface.createCab(
                accessToken = accessToken,
                manufactureId = getRequestBody(manufacture),
                vehicleId = getRequestBody(vehicle),
                registrationNumber = getRequestBody(regNumber),
                seatingCapacity = getRequestBody(seatingCapacity),
                cabType = getRequestBody(cabType),
                isActive = getRequestBody(isActive),
                image = uploadCabFrontImg,
                cab_side_pic = uploadCabSideImg,
                upload_cab_docs = uploadCabDocsImg
            )
            withContext(Dispatchers.Main) { p0(response) }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) { p1(e) }
        }
    }

    suspend fun hitDriverListApi(
        accessToken: String?,
        page_no: Int? = null,
        count: Int? = null,
        p0: (DriverResponse) -> Unit,
        p1: (Exception) -> Unit
    ) {
        try {
            val response = servicesInterface.hitDriverListApi(
                accessToken = accessToken,
                pageNumber = page_no,
                count = count
            )
            withContext(Dispatchers.Main) { p0(response) }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) { p1(e) }
        }
    }

    suspend fun hitFilteredBookingApi(
        accessToken: String?,
        page_no: Int? = null,
        count: Int? = null,
        mediaData: ResponseFilterBody,
        p0: (ResponseBookingList) -> Unit,
        p1: (Exception) -> Unit
    ) {
        try {
            val response = servicesInterface.hitFilteredBookingApi(
                accessToken = accessToken,
                pageNumber = page_no,
                count = count,
                type = mediaData.typeOfFilter,
                starting_day = mediaData.startingDay,
                end_day = mediaData.endDay,
                lastOrUpcomingDate = mediaData.lastOrUpcomingDate
            )
            withContext(Dispatchers.Main) { p0(response) }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) { p1(e) }
        }
    }

    suspend fun getProfileApi(
        accessToken: String?,
        p0: (UserPersonalResponse) -> Unit,
        p1: (Exception) -> Unit
    ) {
        try {
            val response = servicesInterface.getProfileApi(
                accessToken = accessToken
            )
            withContext(Dispatchers.Main) { p0(response) }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) { p1(e) }
        }
    }


}