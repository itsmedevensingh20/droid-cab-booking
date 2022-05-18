package com.cabbooking.ui.cab

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListPopupWindow
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import checkInternet
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.cabbooking.R
import com.cabbooking.base.BaseFragment
import com.cabbooking.constant.Constants
import com.cabbooking.databinding.FragmentCreateCabBinding
import com.cabbooking.interfaces.MyDialogListener
import com.cabbooking.interfaces.MySpinnerListener
import com.cabbooking.models.CabNDriverListResponseData
import com.cabbooking.models.Manufacture
import com.cabbooking.models.ManufactureData
import com.cabbooking.roomDB.viewmodel.UserViewModel
import com.cabbooking.ui.more.CreateActivity
import dagger.hilt.android.AndroidEntryPoint
import getImageInMultiPart
import getString
import orIntZero
import showMenu
import showSnackBar
import showSuccessDialog
import showToast

@AndroidEntryPoint
class CreateCabFragment : BaseFragment<FragmentCreateCabBinding>() {

    private val mActivity by lazy {
        activity as CreateActivity
    }
    private var mCabListResponse: ArrayList<CabNDriverListResponseData> = ArrayList()
    private var mCabManufactureListResponse: ArrayList<ManufactureData> = ArrayList()
    private var mCabSelectedListResponse: ArrayList<Manufacture> = ArrayList()

    private val viewModel: CabViewModel by viewModels()
    private val roomViewModel: UserViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewModel()
        initControl()
    }

    override val layoutRes: Int
        get() = R.layout.fragment_create_cab

    override fun bindViewModel() {
        binding.cabViewModel = viewModel
        binding.lifecycleOwner = this
    }

    private fun initControl() {
        viewModel.accessToken =
            "${Constants.TOKEN_AUTH} ${roomViewModel.getUserData()?.userToken.orEmpty()}"
        inItLiveDataObserver()
    }

    private var uploadCabFrontImg: String = ""
    var uploadCabSideImg: String = ""
    var uploadCabDocsImg: String = ""
    var manufactureId: String? = null
    var vehicleId: String? = null
    var cabStatus: String? = null

    private fun inItLiveDataObserver() {
        viewModel.onCLick.observe(this) {
            when (it?.id) {
                R.id.uploadCabFrontCl, R.id.uploadCabFrontDummy -> {
                    (mActivity).mAskPermission(1, Constants.CREATE_CAB)
                }
                R.id.uploadCabBackCl, R.id.uploadCabBackDummy -> {
                    (mActivity).mAskPermission(2, Constants.CREATE_CAB)
                }
                R.id.uploadCabDocsCl, R.id.uploadCabDocsDummy -> {
                    (mActivity).mAskPermission(3, Constants.CREATE_CAB)
                }

                R.id.manufacture -> {
                    if (checkInternet(requireContext())) {
                        mCabManufactureListResponse.clear()
                        viewModel.getManufactureList()
                    }
                }
                R.id.createCabBtn -> {
                    if (checkInternet(
                            requireContext()
                        )
                    ) {
                        if (isValid()) {
                            viewModel.createCab(
                                manufactureId,
                                vehicleId,
                                binding.seatingCapacity.getString(),
                                binding.cabType.getString(),
                                binding.regNumber.getString(),
                                cabStatus,
                                if (uploadCabFrontImg.isEmpty()) null else getImageInMultiPart(
                                    uploadCabFrontImg,
                                    "image"
                                ),
                                if (uploadCabSideImg.isEmpty()) null else getImageInMultiPart(
                                    uploadCabSideImg,
                                    "cab_side_pic"
                                ),
                                if (uploadCabDocsImg.isEmpty()) null else getImageInMultiPart(
                                    uploadCabDocsImg,
                                    "upload_cab_docs"
                                )
                            )
                        }
                    }
                }
                R.id.active -> {
                    val statusList: ArrayList<String> = ArrayList()
                    statusList.clear()
                    statusList.addAll(resources.getStringArray(R.array.statusList))
                    showMenu(
                        requireContext(),
                        statusList,
                        statusList,
                        binding.active,
                        object : MySpinnerListener {
                            override fun onSelect(s: String, position: String) {
                                if (s == "Active") {
                                    cabStatus = "Yes"
                                } else if (s == "Not Active") {
                                    cabStatus = "No"
                                }
                                binding.active.text = s
                            }
                        })
                }
                R.id.vehicle -> {
                    val mCabList: ArrayList<String> = ArrayList()
                    if (mCabSelectedListResponse.isNotEmpty()) {
                        mCabList.clear()
                        for (i in mCabSelectedListResponse.indices) {
                            mCabList.add(mCabSelectedListResponse.getOrNull(i)?.title.toString())
                        }
                    }
                    showItemMenu(mCabList, binding.vehicle)
                }
            }
        }

//        {"message":"Cab successfully added","status":200,"data":{"menufecturer_id":"4","vehicle_id":"25",
//        "seating_capacity":"5","cab_type":"Hatchback","registration_number":"G4fffyf","image":"media\/230322_13_00_33_25__cab.jpg",
//        "company_id":"42","is_active":"Active","cab_side_pic":"media\/230322_13_00_33_25__cab.jpg","upload_cab_docs":"media\/230322_13_00_33_25__cab.jpg",
//        "updated_at":"2022-03-23T08:03:00.000000Z","created_at":"2022-03-23T08:03:00.000000Z","id":31}}
        viewModel.responseList.observe(requireActivity(), Observer {
            it?.let {
                if (it.status == 200 && it.message == "Cab successfully added") {
                    showSuccessDialog(
                        "Your cab is been register with us, we will verify and come back to you soon",
                        requireContext(),
                        object : MyDialogListener {
                            override fun onResult(result: Any?) {
                                mActivity.goBack()
                            }
                        })
                }
            }
        })
        viewModel.responseManufactureList.observe(requireActivity(), Observer {
            it?.let {
                if (it.status == 200 && it.data?.size.orIntZero() > 0) {
                    mCabManufactureListResponse.clear()
                    mCabSelectedListResponse.clear()
                    if (it.message == Constants.CAB_DATA) {
                        it.data?.let { it1 -> mCabManufactureListResponse.addAll(it1) }
                        val mCabList: ArrayList<String> = ArrayList()
                        val mCabListId: ArrayList<String> = ArrayList()
                        mCabList.clear()
                        mCabListId.clear()
                        if (mCabManufactureListResponse.isNotEmpty()) {
                            for (i in mCabManufactureListResponse.indices) {
                                mCabList.add(mCabManufactureListResponse.getOrNull(i)?.manufactureName.toString())
                                mCabListId.add(mCabManufactureListResponse.getOrNull(i)?.manufactureId.toString())
                            }
                            showMenu(
                                requireContext(),
                                mCabList,
                                mCabListId,
                                binding.manufacture,
                                object : MySpinnerListener {
                                    override fun onSelect(s: String, position: String) {
                                        binding.manufacture.text = s
                                        manufactureId = mCabListId[position.toInt()]
                                        binding.vehicle.text = ""
                                        mCabSelectedListResponse.clear()
                                        binding.isCabSeatCapacity = false
                                        binding.vehicle.hint = getString(R.string.select_vehicle)
                                        binding.vehicle.clearFocus()
                                        try {
                                            val pos = position.toInt() - 1
                                            if (mCabManufactureListResponse[pos].manufactureList.isNotEmpty()) {
                                                mCabSelectedListResponse.addAll(
                                                    mCabManufactureListResponse[pos].manufactureList
                                                )
                                            }
                                        } catch (e: Exception) {
                                        }
                                    }
                                })

                        } else {
                            showToast("Some thing went wrong, Please restart App", mActivity)
                        }
                    }

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

    private fun showItemMenu(
        array: ArrayList<String>,
        view: View
    ) {
        val popupMenu = ListPopupWindow(requireContext())
        popupMenu.setAdapter(
            ArrayAdapter(
                requireContext(),
                R.layout.spinner_dropdown,
                array
            )
        )
        popupMenu.anchorView = view
        popupMenu.verticalOffset = -20
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            popupMenu.setBackgroundDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.popup_solid_curve
                )
            )
        }

        popupMenu.isModal = true
        popupMenu.setOnDismissListener {
        }
        popupMenu.setOnItemClickListener { _, _, position, _ ->
            try {
                binding.vehicle.text =
                    mCabSelectedListResponse[position].title
                binding.isCabSeatCapacity = true
                binding.seatingCapacity.text =
                    mCabSelectedListResponse[position].seats
                binding.cabType.text =
                    mCabSelectedListResponse[position].type
                this.vehicleId = mCabSelectedListResponse[position].id
            } catch (e: Exception) {
                e.printStackTrace()
            }
            popupMenu.dismiss()
            popupMenu.clearListSelection()

        }
        popupMenu.show()

    }


    fun setSelectedImage(it: String, isImgSelected: Int) {
        try {
            when (isImgSelected) {
                1 -> {
                    uploadCabFrontImg = it
                    binding.isCabFrontUploaded = true
                    Glide.with(this)
                        .load(it)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.placeholder)
                        .transform(FitCenter(), RoundedCorners(10))
                        .into(binding.uploadCabFront)
                }
                2 -> {
                    uploadCabSideImg = it
                    Glide.with(this)
                        .load(it)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.placeholder)
                        .transform(FitCenter(), RoundedCorners(10))
                        .into(binding.uploadCabBack)
                    binding.isCabSideUploaded = true

                }
                3 -> {
                    uploadCabDocsImg = it
                    Glide.with(this)
                        .load(it)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.placeholder)
                        .transform(FitCenter(), RoundedCorners(10))
                        .into(binding.uploadCabDocs)
                    binding.isCabDocs = true

                }
            }
        } catch (e: Exception) {
        }
    }

    private fun isValid(): Boolean {
        with(binding)
        {
            when {
                manufacture.getString().isEmpty() -> {
                    view?.rootView?.showSnackBar("Please select manufacture type")
                    return false
                }
                vehicle.getString().isEmpty() -> {
                    view?.rootView?.showSnackBar("Please select vehicle type")
                    return false
                }
                regNumber.getString().isEmpty() -> {
                    view?.rootView?.showSnackBar("Please enter Registration Number")
                    return false
                }
                uploadCabFrontImg.getString().isEmpty() -> {
                    view?.rootView?.showSnackBar("Please upload Cab Front Image")
                    return false
                }
                uploadCabSideImg.getString().isEmpty() -> {
                    view?.rootView?.showSnackBar("Please upload Cab Side Image")
                    return false
                }
                uploadCabDocsImg.getString().isEmpty() -> {
                    view?.rootView?.showSnackBar("Please upload Cab Others Image")
                    return false
                }
                else -> {
                    return true
                }
            }
        }
    }

}