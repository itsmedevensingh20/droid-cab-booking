package com.cabbooking.ui.createAccount

import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cabbooking.base.BaseViewModel
import com.cabbooking.models.BaseRes
import com.cabbooking.models.CommonResponse
import com.cabbooking.models.StateResponse
import com.cabbooking.retrofit.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import getString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class CreateAccountViewModel @Inject constructor(
    private val apiRepository: ApiRepository
) :
    BaseViewModel() {
    private val _onClick = MutableLiveData<View>()
    val onCLick: LiveData<View> = _onClick

    fun onClick(view: View) {
        _onClick.value = view
    }

    // LiveDataFields
    private val _response = MutableLiveData<BaseRes<CommonResponse>>()
    val response: LiveData<BaseRes<CommonResponse>> = _response

    private val _responseVender = MutableLiveData<BaseRes<CommonResponse>>()
    val responseVender: LiveData<BaseRes<CommonResponse>> = _responseVender

    private val _responseState = MutableLiveData<BaseRes<ArrayList<StateResponse>>>()
    val responseState: LiveData<BaseRes<ArrayList<StateResponse>>> = _responseState

    private val _responseDistrict = MutableLiveData<BaseRes<ArrayList<StateResponse>>>()
    val responseDistrict: LiveData<BaseRes<ArrayList<StateResponse>>> = _responseDistrict

    private val _responseCity = MutableLiveData<BaseRes<ArrayList<StateResponse>>>()
    val responseCity: LiveData<BaseRes<ArrayList<StateResponse>>> = _responseCity

    var stateId = ""
    var districtId = ""

    var userName: ObservableField<String> = ObservableField("")
    var companyName: ObservableField<String> = ObservableField("")
    var pinCode: ObservableField<String> = ObservableField("")
    var licenceNumber: ObservableField<String> = ObservableField("")
    var adharNumber: ObservableField<String> = ObservableField("")
    var emailID: ObservableField<String> = ObservableField("")
    var mobileNumber: ObservableField<String> = ObservableField("")


    fun hitCreateDriverAccountApi(
        license_image: MultipartBody.Part?,
        adharcard_front: MultipartBody.Part?,
        adharcard_back: MultipartBody.Part?
    ) {
        showProgress()
        val userData = CommonResponse(
            name = userName.getString(),
            phone = mobileNumber.getString(),
            licenseNo = licenceNumber.getString(),
            adharCardNo = adharNumber.getString()
        )
        viewModelScope.launch(Dispatchers.IO) {
            apiRepository.driverRegistration(
                userData,
                license_image,
                adharcard_front,
                adharcard_back,
                {
                    _response.value = it
                    hideProgress()
                },
                {
                    hideProgressAndShowError(it)
                })
        }
    }

    fun hitCreateVenderAccountApi(
    ) {
        showProgress()
        val userData = CommonResponse(
            name = userName.getString(),
            phone = mobileNumber.getString(),
            companyName = companyName.getString(),
            gstNumber = "gstNumber"
        )
        viewModelScope.launch(Dispatchers.IO) {
            apiRepository.hitCreateVenderAccountApi(userData, {
                _responseVender.value = it
                hideProgress()
            }, {
                hideProgressAndShowError(it)
            })

        }
    }

    fun getAllStates(
    ) {
        showProgress()
        viewModelScope.launch(Dispatchers.IO) {
            apiRepository.getAllStates({
                _responseState.value = it
                hideProgress()
            }, {
                hideProgressAndShowError(it)
            })

        }
    }

    fun getSelectedDistrict(
    ) {
        showProgress()
        viewModelScope.launch(Dispatchers.IO) {
            apiRepository.getSelectedDistrict(stateId, {
                _responseDistrict.value = it
                hideProgress()
            }, {
                hideProgressAndShowError(it)
            })

        }
    }

    fun getSelectedCity(
    ) {
        showProgress()
        viewModelScope.launch(Dispatchers.IO) {
            apiRepository.getSelectedCity(districtId, {
                _responseCity.value = it
                hideProgress()
            }, {
                hideProgressAndShowError(it)
            })
        }
    }
}