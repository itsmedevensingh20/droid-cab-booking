package com.cabbooking.base

import android.text.InputFilter
import androidx.lifecycle.*
import com.cabbooking.retrofit.ResponseHandler
import kotlinx.coroutines.Dispatchers


abstract class BaseViewModel() : ViewModel() {

    private val _progress = MutableLiveData<Boolean>()
    val progress: LiveData<Boolean> = _progress

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

//    private val _netWorkConnection = ConnectionLiveData(androidContext)
//    val netWorkConnection: ConnectionLiveData = _netWorkConnection

    private val _updateHomeData = MutableLiveData<Boolean>()
    val updateHomeData: LiveData<Boolean> = _updateHomeData


    fun showProgress() {
        _progress.value = true
    }

    fun hideProgress() {
        _progress.value = false
    }

    fun hideProgressAndShowError(it: Exception) {
        _progress.value = false
        _error.value = ResponseHandler.ApiErrorHandler.handleException(it)
    }

    fun showError(error: String) {
        _error.value = error
    }

    var userToken: String? = null


    val disableSpace = arrayOf(InputFilter { source, start, end, dest, dstart, dend ->
        for (i in start until end) {
            if (Character.isWhitespace(source[i])) {
                return@InputFilter ""
            }
        }
        null
    })


    fun loadData() = liveData(Dispatchers.IO) {
        emit(null)
    }


}