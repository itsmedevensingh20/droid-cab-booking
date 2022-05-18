package com.cabbooking.ui.booking

import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cabbooking.base.BaseViewModel
import com.cabbooking.base.SingleLiveEvent
import com.cabbooking.models.*
import com.cabbooking.retrofit.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookingViewModel @Inject constructor(
    private val apiRepository: ApiRepository
) : BaseViewModel() {

    private val _response = MutableLiveData<ResponseBookingList>()
    val response: MutableLiveData<ResponseBookingList> = _response

    private val _responseData = MutableLiveData<ResponseBookingData>()
    val responseData: MutableLiveData<ResponseBookingData> = _responseData

    var userName: ObservableField<String> = ObservableField("")
    var mobileNumber: ObservableField<String> = ObservableField("")

    private val _responseList = SingleLiveEvent<CabNDriverListResponse>()
    val responseList: SingleLiveEvent<CabNDriverListResponse> = _responseList

    private val _responsePickDropList = SingleLiveEvent<ResponseStateCity>()
    val responsePickDropList: SingleLiveEvent<ResponseStateCity> = _responsePickDropList


    private val _onClick = SingleLiveEvent<View>()

    val onCLick: SingleLiveEvent<View> = _onClick

    fun onClick(view: View) {
        _onClick.setValue(view)
    }

    var accessToken: String? = null


    fun hitBookingApi(page_no: Int? = null, count: Int? = null) {
        showProgress()
        viewModelScope.launch(Dispatchers.IO) {
            apiRepository.hitBookingApi(accessToken, page_no, count, {
                _response.value = it
                hideProgress()
            }, {
                hideProgressAndShowError(it)
            })
        }
    }


    private val _responseFiltered = MutableLiveData<ResponseBookingList>()
    val responseFiltered: MutableLiveData<ResponseBookingList> = _responseFiltered

    fun hitFilteredBookingApi(
        page_no: Int? = null,
        count: Int? = null,
        mediaData: ResponseFilterBody
    ) {
        showProgress()
        viewModelScope.launch(Dispatchers.IO) {
            apiRepository.hitFilteredBookingApi(
                accessToken,
                page_no,
                count,
                mediaData,
                {
                    _responseFiltered.value = it
                    hideProgress()
                },
                {
                    hideProgressAndShowError(it)
                })
        }
    }

    fun createBooking(
        enquiryDate: String? = null,
        tripType: String? = null,
        startDate: String? = null,
        endDate: String? = null,
        numberOfDays: Long? = null,
        pickupLocation: String? = null,
        dropLocation: String? = null,
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
        lead_detail: String? = null
    ) {
        showProgress()
        viewModelScope.launch(Dispatchers.IO) {
            apiRepository.createBooking(accessToken,
                enquiryDate,
                startDate,
                endDate,
                numberOfDays,
                pickupLocation,
                dropLocation,
                tripType,
                partyName,
                partyPhone,
                cabType,
                numberOfPassenger,
                status,
                bookingAmount,
                advanceAmount,
                pendingAmount,
                cabId,
                driverId,
                comments,
                lead_source,
                lead_detail,
                {
                    _responseData.value = it
                    hideProgress()
                }, {
                    hideProgressAndShowError(it)
                })
        }
    }

    fun getCabList(cabList: Int, driverList: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            apiRepository.getCabNDriverList(accessToken, cabList, driverList, {
                _responseList.setValue(it)
                hideProgress()
            }, {
                hideProgressAndShowError(it)
            })
        }
    }

    fun getPickUpDrop(searchQuery: String) {
        viewModelScope.launch(Dispatchers.IO) {
            apiRepository.getPickUpDrop(accessToken, searchQuery, {
                _responsePickDropList.setValue(it)
                hideProgress()
            }, {
                hideProgressAndShowError(it)
            })
        }
    }
}