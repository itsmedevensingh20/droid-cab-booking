package com.cabbooking.ui.profile

import android.view.View
import androidx.lifecycle.viewModelScope
import com.cabbooking.base.BaseViewModel
import com.cabbooking.base.SingleLiveEvent
import com.cabbooking.models.UserPersonalResponse
import com.cabbooking.retrofit.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val apiRepository: ApiRepository) :
    BaseViewModel() {

    private val _response = SingleLiveEvent<UserPersonalResponse>()
    val response: SingleLiveEvent<UserPersonalResponse> = _response


    private val _onClick = SingleLiveEvent<View>()
    val onCLick: SingleLiveEvent<View> = _onClick

    fun onClick(view: View) {
        _onClick.setValue(view)
    }

    var accessToken: String? = null

    fun getProfileApi() {
        showProgress()
        viewModelScope.launch(Dispatchers.IO) {
            apiRepository.getProfileApi(accessToken, {
                _response.setValue(it)
                hideProgress()
            }, {
                hideProgressAndShowError(it)
            })
        }
    }
}