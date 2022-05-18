package com.cabbooking.ui.booking

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import checkInternet
import com.cabbooking.R
import com.cabbooking.base.BaseFragment
import com.cabbooking.constant.Constants
import com.cabbooking.databinding.FragmentCityStateSelectionBinding
import com.cabbooking.interfaces.CommonListener
import com.cabbooking.models.ResponseStateCityList
import com.cabbooking.roomDB.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import orIntZero
import java.util.*

@AndroidEntryPoint
class CityStateSelectionFragment : BaseFragment<FragmentCityStateSelectionBinding>(),
    CommonListener {
    private val viewModel: BookingViewModel by viewModels()
    private val roomViewModel: UserViewModel by viewModels()

    override val layoutRes: Int
        get() = R.layout.fragment_city_state_selection

    override fun bindViewModel() {
        binding.bookingViewModel = viewModel
        binding.lifecycleOwner = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewModel()
        initControl()
    }

    private fun initControl() {
        viewModel.accessToken =
            "${Constants.TOKEN_AUTH} ${roomViewModel.getUserData()?.userToken.orEmpty()}"
        searchWord()
        inItLiveDataObserver()
    }


    private fun searchWord() {
        binding.stateName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (charSequence?.length.orIntZero() >= 3) {
                    val searchQuery = charSequence.toString().trim().lowercase(Locale.getDefault())
                    callApi(searchQuery)

                } else if (charSequence?.isEmpty() == true) {

                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

    private fun callApi(searchQuery: String) {
        if (checkInternet(requireContext())) {
            viewModel.getPickUpDrop(searchQuery)
        }
    }

    private fun inItLiveDataObserver() {
        viewModel.onCLick.observe(this, Observer {
            when (it.id) {
            }
        })
        viewModel.responsePickDropList.observe(this, Observer {
            it?.let {
                if (it.status == 200 && it.data?.size.orIntZero() > 0) {
//                    it.data?.let { it1 -> mSetAdapter(it1) }
                }
            }
        })
    }
//    private fun mSetAdapter(data: ArrayList<ResponseStateCityList>) {
//        val adp = PickUpDropAdapter()
//        binding.rvStateList.adapter = adp
//        adp.setData(data, this)
//    }
}