package com.cabbooking.ui.filter

import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import androidx.fragment.app.viewModels
import checkInternet
import com.cabbooking.R
import com.cabbooking.base.BaseFragment
import com.cabbooking.constant.Constants
import com.cabbooking.databinding.FragmentFilterBinding
import com.cabbooking.roomDB.viewmodel.UserViewModel
import com.cabbooking.ui.more.HomeVenderActivity
import com.cabbooking.ui.booking.BookingViewModel
import dagger.hilt.android.AndroidEntryPoint
import getTimeStamp
import orIntZero
import orLongZero
import showToast


@AndroidEntryPoint
class FilterFragment :
    BaseFragment<FragmentFilterBinding>(),
    CompoundButton.OnCheckedChangeListener {

    private val viewModel: BookingViewModel by viewModels()
    private val roomViewModel: UserViewModel by viewModels()

    private val mActivity by lazy {
        activity as HomeVenderActivity
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewModel()
        init()
        initControl()
    }

    fun init() {
        with(binding)
        {
            confirmed.setOnCheckedChangeListener(this@FilterFragment)
            pending.setOnCheckedChangeListener(this@FilterFragment)
            notConfirmed.setOnCheckedChangeListener(this@FilterFragment)
            cancel.setOnCheckedChangeListener(this@FilterFragment)
            all.setOnCheckedChangeListener(this@FilterFragment)
        }
    }

    override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
        with(binding)
        {
            if (all.isChecked) {
                confirmed.isChecked = true
                pending.isChecked = true
                notConfirmed.isChecked = true
                cancel.isChecked = true
            }
        }
    }


    fun initControl() {
        inItLiveDataObserver()
    }

    override val layoutRes: Int
        get() = R.layout.fragment_filter

    override fun bindViewModel() {
        binding.filterViewModel = viewModel
        binding.lifecycleOwner = this
    }

    private fun inItLiveDataObserver() {
        viewModel.onCLick.observe(this) {
            when (it?.id) {
                R.id.backBtn -> {
                    clearFilters()
                    mActivity.removeFragment()
                }
                R.id.coming7daysText -> {
                    resetValue()
                    binding.isComing7daysText = true
                    endDay = getTimeStamp(7).orLongZero()
                }
                R.id.coming15daysText -> {
                    resetValue()
                    binding.isComing15daysText = true
                    endDay = getTimeStamp(15).orLongZero()
                }
                R.id.coming30daysText -> {
                    resetValue()
                    binding.isComing30daysText = true
                    endDay = getTimeStamp(30).orLongZero()
                }
                R.id.last7daysText -> {
                    resetValue()
                    binding.isLast7daysText = true
                    startingDay = getTimeStamp(-7).orLongZero()

                }
                R.id.last15daysText -> {
                    resetValue()
                    binding.isLast15daysText = true
                    startingDay = getTimeStamp(-15).orLongZero()
                }
                R.id.last30daysText -> {
                    resetValue()
                    binding.isLast30daysText = true
                    startingDay = getTimeStamp(-30).orLongZero()
                }
                R.id.customText -> {
                    resetValue()
                    binding.isCustomText = true
                    startingDay = 1234567891011
                }
                R.id.applyFilter -> {
                    if (checkInternet(mActivity)) {
                        passData()
                    }
                }
                R.id.clearFilter -> {
                    clearFilters()
                }
            }
        }
    }

    private fun clearFilters() {
        type?.clear()
        startingDay = 0
        endDay = 0
        lastOrUpcomingDate = 0
        resetValue()
        resetCheckBoxValue()
        mActivity.isFilterVisible = false
    }

    private fun resetValue() {
        with(binding)
        {
            isComing7daysText = false
            isComing15daysText = false
            isComing30daysText = false
            isLast7daysText = false
            isLast15daysText = false
            isLast30daysText = false
            isCustomText = false
        }
    }

    private fun resetCheckBoxValue() {
        with(binding)
        {
            confirmed.isChecked = false
            pending.isChecked = false
            notConfirmed.isChecked = false
            cancel.isChecked = false
            all.isChecked = false
        }
    }


    private var startingDay: Long? = 0
    private var endDay: Long? = 0
    private var lastOrUpcomingDate: Long? = 0
    var type: ArrayList<String>? = ArrayList()

    private fun addDataInList() {
        with(binding)
        {
            if (confirmed.isChecked) {
                type?.add(Constants.CONFIRMED)
            } else {
                type?.remove(Constants.CONFIRMED)
            }
            if (pending.isChecked) {
                type?.add(Constants.PENDING)
            } else {
                type?.remove(Constants.PENDING)
            }
            if (notConfirmed.isChecked) {
                type?.add(Constants.NOT_CONFIRMED)
            } else {
                type?.remove(Constants.NOT_CONFIRMED)
            }
            if (cancel.isChecked) {
                type?.add(Constants.CANCEL)
            } else {
                type?.remove(Constants.CANCEL)
            }
            if (all.isChecked) {
                type?.add(Constants.ALL)
            } else {
                type?.remove(Constants.ALL)
            }
        }
    }

    private fun passData() {
        addDataInList()
        if (mValidate()) {
            mActivity.passFilteredData(
                startingDay,
                endDay,
                type,
                lastOrUpcomingDate,
            )
//            type?.clear()
        }
    }

//    private fun passFilteredData(
//        startingDay: Long?,
//        endDay: Long?,
//        type: java.util.ArrayList<String>?,
//        lastOrUpcomingDate: Long?
//    ) {
//        mActivity.passFilteredData()
//        setResult(Activity.RESULT_OK, Intent().apply {
//            putExtra(Constants.STARTING_DAY, startingDay)
//            putExtra(Constants.END_DAY, endDay)
//            putExtra(Constants.TYPE, type)
//            putExtra(Constants.LAST_UPCOMING_DAY, lastOrUpcomingDate)
//        })

//    }

    private fun mValidate(): Boolean {
        if (startingDay == 0L && endDay == 0L && lastOrUpcomingDate == 0L) {
            mActivity.showToast("Please Select Days Type")
            return false
        } else if (type?.isEmpty() == true || type?.size.orIntZero() < 0) {
            mActivity.showToast("Please Select Filter Type")
            return false
        }
        return true
    }

}