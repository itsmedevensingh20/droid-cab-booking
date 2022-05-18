package com.cabbooking.ui.driver

import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.cabbooking.base.BaseViewModel
import com.cabbooking.base.SingleLiveEvent
import com.cabbooking.models.CabNDriverListResponse
import com.cabbooking.models.CreateCabNDriverResponse
import com.cabbooking.models.DriverResponse
import com.cabbooking.retrofit.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class DriverViewModel @Inject constructor(private val apiRepository: ApiRepository) :
    BaseViewModel() {

    private val _response = SingleLiveEvent<DriverResponse>()
    val response: SingleLiveEvent<DriverResponse> = _response

    var userName: ObservableField<String> = ObservableField("")
    var mobileNumber: ObservableField<String> = ObservableField("")
    var licenceNumber: ObservableField<String> = ObservableField("")

    private val _responseList = SingleLiveEvent<CreateCabNDriverResponse>()
    val responseList: SingleLiveEvent<CreateCabNDriverResponse> = _responseList


    private val _onClick = SingleLiveEvent<View>()
    val onCLick: SingleLiveEvent<View> = _onClick

    fun onClick(view: View) {
        _onClick.setValue(view)
    }

    var accessToken: String? = null

    fun hitDriverListApi(page_no: Int?, count: Int) {
        showProgress()
        viewModelScope.launch(Dispatchers.IO) {
            apiRepository.hitDriverListApi(accessToken, page_no, count, {
                _response.setValue(it)
                hideProgress()
            }, {
                hideProgressAndShowError(it)
            })
        }
    }

//    fun getDriverList(cabList: Int, driverList: Int) {
//        showProgress()
//        viewModelScope.launch(Dispatchers.IO) {
//            apiRepository.getCabNDriverList(accessToken, cabList, driverList, {
//                _responseList.setValue(it)
//                hideProgress()
//            }, {
//                hideProgressAndShowError(it)
//            })
//        }
//    }

    fun createDriver(
        name: String? = null,
        phone: String? = null,
        email: String? = null,
        licenseNumber: String? = null,
        isActive: String? = null,
        driverLicenceImg: MultipartBody.Part?,
        driverImg: MultipartBody.Part?
    ) {
        showProgress()
        viewModelScope.launch(Dispatchers.IO) {
            apiRepository.createDriver(
                accessToken,
                name,
                phone,
                email,
                licenseNumber,
                isActive,
                driverLicenceImg,
                driverImg,
                {
                    _responseList.setValue(it)
                    hideProgress()
                },
                {
                    hideProgressAndShowError(it)
                })
        }
    }

}