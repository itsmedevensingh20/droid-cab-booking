package com.cabbooking.ui.more

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cabbooking.base.BaseViewModel
import com.cabbooking.models.ResponseBookingList
import com.cabbooking.retrofit.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val apiRepository: ApiRepository) :
    BaseViewModel() {

    private val _onClick = MutableLiveData<View>()
    val onCLick: LiveData<View> = _onClick


    fun onClick(view: View) {
        _onClick.value = view
    }



}