package com.cabbooking.ui.booking

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import checkInternet
import com.cabbooking.R
import com.cabbooking.base.BaseFragment
import com.cabbooking.constant.Constants
import com.cabbooking.databinding.FragmentCreateBookingBinding
import com.cabbooking.interfaces.CommonListener
import com.cabbooking.interfaces.MyDialogListener
import com.cabbooking.interfaces.MySpinnerListener
import com.cabbooking.models.CabNDriverListResponseData
import com.cabbooking.models.ResponseStateCityList
import com.cabbooking.roomDB.viewmodel.UserViewModel
import com.cabbooking.ui.more.CreateActivity
import dagger.hilt.android.AndroidEntryPoint
import doGone
import doVisible
import getCurrentDate
import getDaysBetweenDates
import getString
import getTimeStampFromDate
import isValidMobile
import openCalendar
import orIntZero
import showMenu
import showSnackBar
import showSuccessDialog
import showToast
import java.util.*

@AndroidEntryPoint
class CreateBookingFragment : BaseFragment<FragmentCreateBookingBinding>(), CommonListener {
    private val mActivity by lazy {
        activity as CreateActivity
    }
    private val viewModel: BookingViewModel by viewModels()
    private val roomViewModel: UserViewModel by viewModels()
    private var mCabListResponse: ArrayList<CabNDriverListResponseData> = ArrayList()
    var mDriverListResponse: ArrayList<CabNDriverListResponseData> = ArrayList()
    override val layoutRes: Int
        get() = R.layout.fragment_create_booking

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
        binding.enquiryDate.text = getCurrentDate()
        mSetAdapter()
        searchWord()
        inItLiveDataObserver()
    }

    var isValue: String? = null

    private fun searchWord() {
        binding.isPickUpEnable = true
        binding.isDropEnable = false
        binding.pickupCityName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (charSequence?.length.orIntZero() >= 3) {
                    val searchQuery = charSequence.toString().trim().lowercase(Locale.getDefault())
                    if (!searchQuery.matches(((".*[/\\\\].*").toRegex()))) {
                        callApi(searchQuery)
                        isValue = Constants.PICK_UP
                    }

                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        binding.dropCityName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (charSequence?.length.orIntZero() >= 3) {
                    val searchQuery = charSequence.toString().trim().lowercase(Locale.getDefault())
                    if (!searchQuery.matches(((".*[/\\\\].*").toRegex()))) {
                        callApi(searchQuery)
                        isValue = Constants.DROP
                    }

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

    var bookingStatus: String? = null
    var tripType: String? = null
    var cabId: String? = null
    var driverId: String? = null

    //    var startDate: String? = null
//    var endDate: String? = null
    var numberOfDays: Long? = null
    var enquiryDate: Long? = null

    private fun inItLiveDataObserver() {
        viewModel.onCLick.observe(this, Observer {
            when (it.id) {
                R.id.createBookingBtn -> {
                    try {
                        if (checkInternet(
                                requireContext()
                            )
                        ) {
                            if (isValid()) {
//                                startDate =  binding.startDate.getString()
//                                endDate = binding.endDate.getString()
                                numberOfDays = getDaysBetweenDates(
                                    binding.startDate.getString(),
                                    binding.endDate.getString()
                                ) + 1
                                with(binding) {
                                    viewModel.createBooking(
                                        getTimeStampFromDate(enquiryDate.getString()).toString(),
                                        bookingType.getString(),
                                        getTimeStampFromDate(startDate.getString()).toString(),
                                        getTimeStampFromDate(endDate.getString()).toString() ,
                                        numberOfDays,
                                        pickupCityName.getString(),
                                        dropCityName.getString(),
                                        fullName.getString(),
                                        mobileNumber.getString(),
                                        cabType.getString(),
                                        numberOfPass.getString(),
                                        bookingStatus,
                                        fullRate.getString(),
                                        advanceRate.getString(),
                                        pendingRate.getString(),
                                        cabId,
                                        driverId,
                                        comment.getString(),
                                        "Office",
                                        "Call"
                                    )
                                }
                            }
                        }
                    } catch (e: Exception) {
                    }
                }

                R.id.enquiryDate -> {
                    openCalendar(binding.enquiryDate)
                }

                R.id.bookingType -> {
                    val bookingTypeList: ArrayList<String> = ArrayList()
                    bookingTypeList.clear()
                    bookingTypeList.addAll(resources.getStringArray(R.array.bookingTypeList))
                    showMenu(
                        requireContext(),
                        bookingTypeList,
                        bookingTypeList,
                        binding.bookingType,
                        object : MySpinnerListener {
                            override fun onSelect(s: String, position: String) {
                                binding.bookingType.text = s
                                when (s) {
                                    Constants.ONE_WAY -> {
                                        binding.startDate.doVisible()
                                        binding.endDate.doGone()
                                        tripType = Constants.ONE_WAY
                                    }
                                    Constants.ROUND_WAY -> {
                                        binding.startDate.doVisible()
                                        binding.endDate.doVisible()
                                        tripType = Constants.ROUND_WAY

                                    }
                                    Constants.RENTAL -> {
                                        binding.startDate.doVisible()
                                        binding.endDate.doGone()
                                        tripType = Constants.RENTAL

                                    }
                                    Constants.SHARING -> {
                                        binding.startDate.doVisible()
                                        binding.endDate.doGone()
                                        tripType = Constants.SHARING

                                    }
                                    else -> {
                                        binding.startDate.doGone()
                                        binding.endDate.doGone()
                                    }
                                }
                            }
                        })
                }

                R.id.startDate -> {
                    openCalendar(binding.startDate)
                }

                R.id.endDate -> {
                    openCalendar(binding.endDate)
                }

                R.id.selectCab -> {
                    if (checkInternet(requireContext())) {
                        mCabListResponse.clear()
                        viewModel.getCabList(Constants.ONE, 0)
                    }
                }

                R.id.selectDriver -> {
                    if (checkInternet(requireContext())) {
                        mCabListResponse.clear()
                        viewModel.getCabList(0, Constants.ONE)
                    }
                }
//                R.id.pickUpText -> {
//                    mActivity.addFragment()
//                    if (!binding.frameLayout.isVisible) {
//                        binding.frameLayout.doVisible()
//                        val transaction = activity?.supportFragmentManager?.beginTransaction()
//                        transaction?.add(
//                            R.id.frameLayout,
//                            CityStateSelectionFragment(),
//                            "CityStateSelectionFragment"
//                        )?.commit()
//                    }

//                }
//                R.id.dropText -> {
//                    mActivity.addFragment()
//                    if (!binding.frameLayout.isVisible) {
//                        binding.frameLayout.doVisible()
//                        val transaction = activity?.supportFragmentManager?.beginTransaction()
//                        transaction?.add(
//                            R.id.frameLayout,
//                            CityStateSelectionFragment(),
//                            "CityStateSelectionFragment"
//                        )?.commit()
//                    }

//                }

                R.id.cabType -> {
                    val cabTypeList: ArrayList<String> = ArrayList()
                    cabTypeList.clear()
                    cabTypeList.addAll(resources.getStringArray(R.array.cabTypeList))
                    showMenu(
                        requireContext(),
                        cabTypeList,
                        cabTypeList,
                        binding.cabType,
                        object : MySpinnerListener {
                            override fun onSelect(s: String, position: String) {
                                binding.cabType.text = s
                            }
                        })
                }

                R.id.numberOfPass -> {
                    val numberOfPassList: ArrayList<String> = ArrayList()
                    numberOfPassList.clear()
                    numberOfPassList.addAll(resources.getStringArray(R.array.numberOfPassList))
                    showMenu(
                        requireContext(),
                        numberOfPassList,
                        numberOfPassList,
                        binding.numberOfPass,
                        object : MySpinnerListener {
                            override fun onSelect(s: String, position: String) {
                                binding.numberOfPass.text = s
                            }
                        })
                }
                R.id.status -> {
                    val statusList: ArrayList<String> = ArrayList()
                    statusList.clear()
                    statusList.addAll(resources.getStringArray(R.array.statusList))
                    showMenu(
                        requireContext(),
                        statusList,
                        statusList,
                        binding.status,
                        object : MySpinnerListener {
                            override fun onSelect(s: String, position: String) {
                                binding.status.text = s
                                if (s == "Active") {
                                    bookingStatus = "Yes"
                                } else if (s == "Not Active") {
                                    bookingStatus = "No"
                                }
                            }
                        })
                }
            }
        })
        viewModel.responseData.observe(viewLifecycleOwner) {
            it?.let { its ->
                if (its.message == "Booking successfully" && its.status == 200) {
                    showSuccessDialog(
                        "Your Booking is successfully created",
                        requireContext(),
                        object : MyDialogListener {
                            override fun onResult(result: Any?) {
                                mActivity.goBack()
                            }
                        })

                }
            }
        }

        viewModel.responseList.observe(this, Observer {
            it?.let {
                if (it.status == 200 && it.data?.size.orIntZero() > 0) {
                    if (it.message == Constants.DRIVER_LIST) {
                        it.data?.let { it1 -> mDriverListResponse.addAll(it1) }
                        val mDriverList: ArrayList<String> = ArrayList()
                        val mDriverListId: ArrayList<String> = ArrayList()
                        mDriverList.clear()
                        mDriverListId.clear()
                        if (mDriverListResponse.isNotEmpty()) {
                            for (i in mDriverListResponse.indices) {
                                mDriverList.add(mDriverListResponse.getOrNull(i)?.driverName.toString())
                                mDriverListId.add(mDriverListResponse.getOrNull(i)?.id.toString())
                            }
                            showMenu(
                                requireContext(),
                                mDriverList,
                                mDriverListId,
                                binding.selectDriver,
                                object : MySpinnerListener {
                                    override fun onSelect(s: String, position: String) {
                                        binding.selectDriver.text = s
                                        driverId = position
                                    }
                                })
                        } else {
                            showToast("Some thing went wrong, Please restart App", mActivity)
                        }
                    }
                    if (it.message == Constants.CAB_LIST) {
                        it.data?.let { it1 -> mCabListResponse.addAll(it1) }
                        val mCabList: ArrayList<String> = ArrayList()
                        val mCabListId: ArrayList<String> = ArrayList()
                        mCabList.clear()
                        mCabListId.clear()
                        if (mCabListResponse.isNotEmpty()) {
                            for (i in mCabListResponse.indices) {
                                mCabList.add(mCabListResponse.getOrNull(i)?.cabType.toString())
                                mCabListId.add(mCabListResponse.getOrNull(i)?.id.toString())
                            }
                            showMenu(
                                requireContext(),
                                mCabList,
                                mCabListId,
                                binding.selectCab,
                                object : MySpinnerListener {
                                    override fun onSelect(s: String, position: String) {
                                        binding.selectCab.text = s
                                        cabId = position
                                    }
                                })

                        } else {
                            showToast("Some thing went wrong, Please restart App", mActivity)
                        }
                    }
                }
            }
        })
        viewModel.responsePickDropList.observe(this, Observer {
            it?.let {
                try {
                    if (it.status == 200 && it.data.size.orIntZero() > 0) {
                        binding.rvStateList.doVisible()
                        mSetAdapter(it.data)
                    } else {
                        binding.rvStateList.doGone()
                    }
                } catch (e: Exception) {
                }
            }
        })
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

    var adp = PickUpDropAdapter()
    val data: ArrayList<ResponseStateCityList> = ArrayList()
    private fun mSetAdapter() {
        binding.rvStateList.adapter = adp
    }

    private fun mSetAdapter(data: ArrayList<ResponseStateCityList>) {
        adp.setData(data, this, isValue)
    }

    override fun onListItemSelect(s: String, position: String) {
        when (position) {
            Constants.PICK_UP -> {
                binding.pickupCityName.setText(s)
                binding.isDropEnable = true
                binding.isPickUpEnable = true
                mSetAdapter(data)
                binding.rvStateList.doGone()
                binding.dropCityName.requestFocus()
            }
            Constants.DROP -> {
                binding.dropCityName.setText(s)
                mSetAdapter(data)
                binding.isPickUpEnable = true
                binding.isDropEnable = true
                binding.rvStateList.doGone()
                binding.fullName.requestFocus()
            }
        }
    }

    /*                when (tripType) {
                    Constants.ONE_WAY -> {
                        if (startDate.getString().isEmpty()) {
                            view.rootView.showSnackBar("Please select start date")
                            return false
                        } else {
                            return true
                        }
                    }
                    Constants.ROUND_WAY -> {
                        if (startDate.getString().isEmpty()) {
                            view.rootView.showSnackBar("Please select start date")
                            return false
                        } else if (endDate.getString().isEmpty()) {
                            view.rootView.showSnackBar("Please select end date")
                            return false
                        } else {
                            return true
                        }

                    }
                    Constants.RENTAL -> {
                        if (startDate.getString().isEmpty()) {
                            view.rootView.showSnackBar("Please select start date")
                            return false
                        } else {
                            return true
                        }
                    }
                    Constants.SHARING -> {
                        if (startDate.getString().isEmpty()) {
                            view.rootView.showSnackBar("Please select start date")
                            return false
                        } else {
                            return true
                        }
                    }
                    else -> {
                        return true
                    }
                }
*/
    private fun isValid(): Boolean {
        with(binding)
        {
            if (enquiryDate.getString().isEmpty()) {
                view.rootView.showSnackBar("Please select enquiry date")
                return false
            } else if (bookingType.getString().isEmpty()) {
                view.rootView.showSnackBar("Please select booking type")
                return false
            } else if (pickupCityName.getString().isEmpty()) {
                view.rootView.showSnackBar("Please enter pick up location")
                return false
            } else if (dropCityName.getString().isEmpty()) {
                view.rootView.showSnackBar("Please enter drop location")
                return false
            } else if (fullName.getString().isEmpty()) {
                view.rootView.showSnackBar("Please enter full name")
                return false
            } else if (mobileNumber.getString().isEmpty()) {
                view.rootView.showSnackBar("Please enter mobile number")
                return false
            } else if (!mobileNumber.getString().isValidMobile) {
                view.rootView.showSnackBar("Please enter valid mobile number")
                return false
            } else if (cabType.getString().isEmpty()) {
                view.rootView.showSnackBar("Please select cab type")
                return false
            } else if (numberOfPass.getString().isEmpty()) {
                view.rootView.showSnackBar("Please select number of passenger")
                return false
            } else if (status.getString().isEmpty()) {
                view.rootView.showSnackBar("Please select booking status")
                return false
            } else if (fullRate.getString().isEmpty()) {
                view.rootView.showSnackBar("Please enter booking rate")
                return false
            } else if (advanceRate.getString().isEmpty()) {
                view.rootView.showSnackBar("Please enter Advance booking rate")
                return false
            } else if (pendingRate.getString().isEmpty()) {
                view.rootView.showSnackBar("Please enter pending booking rate")
                return false
            } else if (selectCab.getString().isEmpty()) {
                view.rootView.showSnackBar("Please select cab")
                return false
            } else if (selectDriver.getString().isEmpty()) {
                view.rootView.showSnackBar("Please select driver")
                return false
            } else {
                return true
            }

//            when {
////                pickupCityName.getString().isEmpty() -> {
////                    view.rootView.showSnackBar("Please enter pick up location")
////                    return false
////                }
////                dropCityName.getString().isEmpty() -> {
////                    view.rootView.showSnackBar("Please enter drop location")
////                    return false
////                }
////                fullName.getString().isEmpty() -> {
////                    view.rootView.showSnackBar("Please enter full name")
////                    return false
////                }
////                mobileNumber.getString().isEmpty() -> {
////                    view.rootView.showSnackBar("Please enter mobile number")
////                    return false
////                }
////                !mobileNumber.getString().isValidMobile -> {
////                    view.rootView.showSnackBar("Please enter valid mobile number")
////                    return false
////                }
////                cabType.getString().isEmpty() -> {
////                    view.rootView.showSnackBar("Please select cab type")
////                    return false
////                }
////                numberOfPass.getString().isEmpty() -> {
////                    view.rootView.showSnackBar("Please select number of passenger")
////                    return false
////                }
////                status.getString().isEmpty() -> {
////                    view.rootView.showSnackBar("Please select booking status")
////                    return false
////                }
////                fullRate.getString().isEmpty() -> {
////                    view.rootView.showSnackBar("Please enter booking rate")
////                    return false
////                }
////                advanceRate.getString().isEmpty() -> {
////                    view.rootView.showSnackBar("Please enter Advance booking rate")
////                    return false
////                }
////                pendingRate.getString().isEmpty() -> {
////                    view.rootView.showSnackBar("Please enter pending booking rate")
////                    return false
////                }
////                selectCab.getString().isEmpty() -> {
////                    view.rootView.showSnackBar("Please select cab")
////                    return false
////                }
////                selectDriver.getString().isEmpty() -> {
////                    view.rootView.showSnackBar("Please select driver")
////                    return false
////                }
//
//            }
        }
    }

}