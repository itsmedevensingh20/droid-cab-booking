package com.cabbooking.ui.booking

import android.os.Bundle
import androidx.activity.viewModels
import com.cabbooking.R
import com.cabbooking.base.BaseActivity
import com.cabbooking.constant.Constants
import com.cabbooking.databinding.ActivityBookingDetailsBinding
import com.cabbooking.roomDB.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import doBack
import kotlinx.android.synthetic.main.layout_toolbar_common.view.*

@AndroidEntryPoint
class BookingDetailsActivity : BaseActivity<ActivityBookingDetailsBinding>() {

    private val roomViewModel: UserViewModel by viewModels()
    private val viewModel: BookingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_details)
    }

    override val layoutRes: Int
        get() = R.layout.activity_create

    override fun bindViewModel() {
        binding.bookingDetailsViewModel = viewModel
        binding.lifecycleOwner = this
    }

    override fun init() {
        binding.toolbarCl.backBtn.doBack()
    }

    override fun initControl() {
        viewModel.accessToken =
            "${Constants.TOKEN_AUTH} ${roomViewModel.getUserData()?.userToken.orEmpty()}"
        inItLiveDataObserver()
    }

    private fun inItLiveDataObserver() {
        viewModel.onCLick.observe(this) {
            when (it.id) {
                R.id.shareToRoute -> {

                }
            }
        }
    }

}