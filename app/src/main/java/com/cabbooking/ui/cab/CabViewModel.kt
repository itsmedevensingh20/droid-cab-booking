package com.cabbooking.ui.cab

import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.cabbooking.base.BaseViewModel
import com.cabbooking.base.SingleLiveEvent
import com.cabbooking.models.CabNDriverListResponse
import com.cabbooking.models.CabResponse
import com.cabbooking.models.CreateCabNDriverResponse
import com.cabbooking.models.ResponseManufacture
import com.cabbooking.retrofit.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class CabViewModel @Inject constructor(private val apiRepository: ApiRepository) : BaseViewModel() {

    private val _response = SingleLiveEvent<CabResponse>()
    val response: SingleLiveEvent<CabResponse> = _response

    private val _responseList = SingleLiveEvent<CreateCabNDriverResponse>()
    val responseList: SingleLiveEvent<CreateCabNDriverResponse> = _responseList

    private val _responseManufactureList = SingleLiveEvent<ResponseManufacture>()
    val responseManufactureList: SingleLiveEvent<ResponseManufacture> = _responseManufactureList

    private val _onClick = SingleLiveEvent<View>()
    val onCLick: SingleLiveEvent<View> = _onClick

    var cabNumb: ObservableField<String> = ObservableField("")
    var regNumber: ObservableField<String> = ObservableField("")

    fun onClick(view: View) {
        _onClick.setValue(view)
    }

    var accessToken: String? = null

    fun hitCabListApi(page_no: Int?, count: Int) {
        showProgress()
        viewModelScope.launch(Dispatchers.IO) {
            apiRepository.hitCabListApi(accessToken, page_no, count, {
                _response.setValue(it)
                hideProgress()
            }, {
                hideProgressAndShowError(it)
            })
        }
    }

//    fun getCabList(cabList: Int, driverList: Int) {
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

    fun getManufactureList() {
        showProgress()
        viewModelScope.launch(Dispatchers.IO) {
            apiRepository.getAllManufacturer(accessToken, {
                _responseManufactureList.setValue(it)
                hideProgress()
            }, {
                hideProgressAndShowError(it)
            })
        }
    }

    fun createCab(
        manufacture: String? = null,
        vehicle: String? = null,
        seatingCapacity: String? = null,
        cabType: String? = null,
        regNumber: String? = null,
        isActive: String? = null,
        uploadCabFrontImg: MultipartBody.Part?,
        uploadCabSideImg: MultipartBody.Part?,
        uploadCabDocsImg: MultipartBody.Part?
    ) {
        showProgress()
        viewModelScope.launch(Dispatchers.IO) {
            apiRepository.createCab(
                accessToken,
                manufacture,
                vehicle,
                seatingCapacity,
                cabType,
                regNumber,
                isActive,
                uploadCabFrontImg,
                uploadCabSideImg,
                uploadCabDocsImg,
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