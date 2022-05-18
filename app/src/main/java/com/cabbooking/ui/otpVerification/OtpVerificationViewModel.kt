package com.cabbooking.ui.otpVerification

import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cabbooking.base.BaseViewModel
import com.cabbooking.models.BaseRes
import com.cabbooking.models.CommonResponse
import com.cabbooking.models.ResponseAuthentication
import com.cabbooking.retrofit.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import getString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OtpVerificationViewModel @Inject constructor( private val apiRepository: ApiRepository) : BaseViewModel() {

    // Observable Fields
    var otp1: ObservableField<String> = ObservableField("")
    var otp2: ObservableField<String> = ObservableField("")
    var otp3: ObservableField<String> = ObservableField("")
    var otp4: ObservableField<String> = ObservableField("")

    private val _response = MutableLiveData<BaseRes<CommonResponse>>()
    val response: LiveData<BaseRes<CommonResponse>> = _response

    private val _responseOTP = MutableLiveData<BaseRes<ResponseAuthentication>>()
    val responseOTP: LiveData<BaseRes<ResponseAuthentication>> = _responseOTP

    private val _onClick = MutableLiveData<View>()
    val onCLick: LiveData<View> = _onClick

    var phoneNumber: String = ""

    fun onClick(view: View) {
        _onClick.value = view
    }

    private fun getOTP(): String {
        return "${otp1.getString()}${otp2.getString()}${otp3.getString()}${otp4.getString()}"
    }

    fun getOtpVerify() {
        showProgress()
        val otpData = CommonResponse(
            otp = getOTP()
        )
        viewModelScope.launch(Dispatchers.IO) {
            apiRepository.hitOTPApi(otpData, {
                _response.value = it
                hideProgress()
            }, {
                hideProgressAndShowError(it)
            })

        }
    }

    fun getOtp() {
        showProgress()
        val otpData = CommonResponse(
            phone = phoneNumber
        )
        viewModelScope.launch(Dispatchers.IO) {
            apiRepository.getOtp(otpData, {
                _responseOTP.value = it
                hideProgress()
            }, {
                hideProgressAndShowError(it)
            })

        }
    }
}