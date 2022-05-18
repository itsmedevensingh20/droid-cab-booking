package com.cabbooking.ui.route

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.cabbooking.R
import com.cabbooking.base.BaseFragment
import com.cabbooking.constant.Constants
import com.cabbooking.databinding.FragmentRouteListBinding
import com.cabbooking.extension.goToNextScreen
import com.cabbooking.roomDB.viewmodel.UserViewModel
import com.cabbooking.ui.booking.BookingDetailsActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RouteListFragment : BaseFragment<FragmentRouteListBinding>() {

    private val viewModel: RouteViewModel by viewModels()
    private val roomViewModel: UserViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewModel()
        initControl()
        inItLiveDataObserver()
    }

    private fun initControl() {
        viewModel.accessToken =
            "${Constants.TOKEN_AUTH} ${roomViewModel.getUserData()?.userToken.orEmpty()}"
    }

    override val layoutRes: Int
        get() = R.layout.fragment_route_list

    override fun bindViewModel() {
        binding.routeViewModel = viewModel
        binding.lifecycleOwner = this
    }

    private fun inItLiveDataObserver() {
        viewModel.onCLick.observe(this) {
            when (it?.id) {
                R.id.plusBid, R.id.minusBid -> {
                    activity?.goToNextScreen(BookingDetailsActivity::class.java)
                }
            }
        }
    }
}