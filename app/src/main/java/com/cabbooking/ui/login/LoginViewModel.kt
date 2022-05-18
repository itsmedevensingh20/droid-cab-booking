package com.cabbooking.ui.login

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
class LoginViewModel @Inject constructor(private val apiRepository: ApiRepository) :
    BaseViewModel() {

    private val _onClick = MutableLiveData<View>()
    val onCLick: LiveData<View> = _onClick

    fun onClick(view: View) {
        _onClick.value = view
    }

    var loginNumber: ObservableField<String> = ObservableField("")

    private val _response = MutableLiveData<BaseRes<ResponseAuthentication>>()
    val response: LiveData<BaseRes<ResponseAuthentication>> = _response

    fun hitLogin() {
        showProgress()
        val loginData = CommonResponse(
            phone = loginNumber.getString()
        )
        viewModelScope.launch(Dispatchers.IO) {
            apiRepository.getOtp(loginData, {
                _response.value = it
                hideProgress()
            }, {
                hideProgressAndShowError(it)
            })
        }
    }
}