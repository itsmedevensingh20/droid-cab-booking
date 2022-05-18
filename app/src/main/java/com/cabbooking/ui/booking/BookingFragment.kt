package com.cabbooking.ui.booking

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import checkInternet
import com.cabbooking.R
import com.cabbooking.base.BaseFragment
import com.cabbooking.constant.Constants
import com.cabbooking.databinding.FragmentBookingBinding
import com.cabbooking.interfaces.CommonListener
import com.cabbooking.models.ResponseBooking
import com.cabbooking.models.ResponseFilterBody
import com.cabbooking.roomDB.viewmodel.UserViewModel
import com.cabbooking.ui.more.CreateActivity
import dagger.hilt.android.AndroidEntryPoint
import doGone
import doVisible
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import orIntZero
import showSnackBar

@AndroidEntryPoint
class BookingFragment : BaseFragment<FragmentBookingBinding>(), CommonListener {

    private val viewModel: BookingViewModel by viewModels()
    private val roomViewModel: UserViewModel by viewModels()
    var nextKey: Int = Constants.ONE
    var nextFilterKey: Int = Constants.ONE

    var mBookingList: ArrayList<ResponseBooking>? = ArrayList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewModel()
        initControl()
    }

    private fun initControl() {
        viewModel.accessToken =
            "${Constants.TOKEN_AUTH} ${roomViewModel.getUserData()?.userToken.orEmpty()}"
        mBookingList?.clear()
        apiCalling(nextKey)
        mSetAdapter()
        handleSwipe()
        inItLiveDataObserver()
    }

    private fun handleSwipe() {
        binding.swipeBooking.setColorSchemeResources(
            R.color.red_text,
            R.color.green_text,
            R.color.color_f9c22d
        )
        if (!binding.swipeBooking.isRefreshing) {
            binding.swipeBooking.setOnRefreshListener {
                passClearFilterData()
                lifecycleScope.launch {
                    delay(Constants.SWIPE_TIME)
                    binding.swipeBooking.isRefreshing = false
                }
            }
        }
    }


    private fun apiCalling(page_no: Int? = null) {
        if (checkInternet(requireActivity())) {
            viewModel.hitBookingApi(page_no, Constants.TEN)
        }
    }

    var isFilteredData: Boolean = false
    private var startingDay: Long? = 0
    private var endDay: Long? = 0
    private var lastOrUpcomingDate: Long? = 0
    var typeOfFilter: ArrayList<String> = ArrayList()

    fun passClearFilterData() {
        nextKey = Constants.ONE
        binding.loadMore.doGone()
        mBookingList?.clear()
        apiCalling(nextKey)
    }

    fun passFilteredData(
        startingDay: Long? = null,
        endDay: Long? = null,
        type: ArrayList<String>? = ArrayList(),
        lastOrUpcomingDate: Long? = null
    ) {
        isFilteredData = true
        binding.loadMore.doGone()
        mBookingList?.clear()
        mUpdateAdapter()
        this.startingDay = startingDay
        this.endDay = endDay
        this.lastOrUpcomingDate = lastOrUpcomingDate
        if (type != null) {
            this.typeOfFilter = type
        }
        apiCallingFilteredList(
            nextFilterKey
        )
    }

    private fun apiCallingFilteredList(
        page_no: Int? = null
    ) {
        if (isAdded) {
            if (checkInternet(requireActivity())) {
                val mediaData = ResponseFilterBody(
                    startingDay = startingDay,
                    typeOfFilter = typeOfFilter,
                    endDay = endDay,
                    lastOrUpcomingDate = lastOrUpcomingDate
                )
                viewModel.hitFilteredBookingApi(
                    page_no,
                    Constants.TEN,
                    mediaData
                )
            }
        }
    }

    override val layoutRes: Int
        get() = R.layout.fragment_booking

    override fun bindViewModel() {
        binding.bookingViewModel = viewModel
        binding.lifecycleOwner = this
    }

    private fun inItLiveDataObserver() {
        viewModel.onCLick.observe(this) {
            when (it?.id) {
                R.id.loadMore -> {
                    if (isFilteredData) {
                        if (nextFilterKey.orIntZero() > Constants.ONE) {
                            apiCallingFilteredList(nextFilterKey)
                        }
                    } else {
                        if (nextKey.orIntZero() > Constants.ONE) {
                            apiCalling(nextKey)
                        }
                    }
                }
            }
        }
        viewModel.response.observe(viewLifecycleOwner) {
            it?.let { its ->
                if (its.statusCode.orIntZero() == 200) {
                    its.bookingList?.let { it ->
                        if (its.bookingList.size.orIntZero() > 0) {
                            binding.noData.doGone()
                            mBookingList?.addAll(it)
                            mUpdateAdapter()
                            if (it.size >= 10) {
                                binding.loadMore.doVisible()
                                nextKey++
                            } else {
                                nextKey = 0
                                binding.loadMore.doGone()
                            }
                        } else {
                            binding.noData.doVisible()
                        }
                    } ?: kotlin.run {
                        binding.noData.doVisible()
                    }
                }
            }
        }
        viewModel.responseFiltered.observe(viewLifecycleOwner) {
            it?.let { its ->
                if (its.status.orIntZero() == 200) {
                    its.bookingDataList?.let { it ->
                        if (its.bookingDataList.size.orIntZero() > 0) {
                            binding.noData.doGone()
                            mBookingList?.addAll(it)
                            mUpdateAdapter()
                            if (it.size >= 10) {
                                binding.loadMore.doVisible()
                                nextFilterKey++
                            } else {
                                nextFilterKey = 0
                                binding.loadMore.doGone()
                            }
                        } else {
                            binding.noData.doVisible()
                        }
                    } ?: kotlin.run {
                        binding.noData.doVisible()
                    }
                }
            }
        }
        viewModel.progress.observe(requireActivity(), Observer {
            if (it) {
                showProgress()
            } else {
                hideProgress()
            }
        })

        viewModel.error.observe(requireActivity(), Observer {
            binding.root.showSnackBar(it)
        })
    }

    private var adp = BookingMonthListAdapter()
    private fun mSetAdapter() {
        binding.bookingListRV.adapter = adp
    }

    private fun mUpdateAdapter() {
        mBookingList?.let { adp.setData(it, this, requireActivity() as CreateActivity) }
    }

    override fun onCLick(position: Int, _id: String, itemName: String) {
        try {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:+$_id"))
            context?.startActivity(intent)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}