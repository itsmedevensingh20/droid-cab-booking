package com.cabbooking.ui.driver

import android.os.Bundle
import android.view.View
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
import com.cabbooking.databinding.FragmentCreateDriverBinding
import com.cabbooking.interfaces.MyDialogListener
import com.cabbooking.interfaces.MySpinnerListener
import com.cabbooking.roomDB.viewmodel.UserViewModel
import com.cabbooking.ui.more.CreateActivity
import dagger.hilt.android.AndroidEntryPoint
import getImageInMultiPart
import getString
import isValidEmail
import isValidMobile
import showMenu
import showSnackBar
import showSuccessDialog

@AndroidEntryPoint
class CreateDriverFragment : BaseFragment<FragmentCreateDriverBinding>() {
    private val viewModel: DriverViewModel by viewModels()
    private val roomViewModel: UserViewModel by viewModels()

    private val mActivity by lazy {
        activity as CreateActivity
    }
    override val layoutRes: Int
        get() = R.layout.fragment_create_driver

    override fun bindViewModel() {
        binding.driverViewModel = viewModel
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
        inItLiveDataObserver()
    }


    private var driverLicenceImg: String = ""
    var uploadDriverImg: String = ""
    var driverStatus: String? = null


    private fun inItLiveDataObserver() {
        viewModel.onCLick.observe(this, Observer {
            when (it.id) {
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
                                binding.active.text = s
                                if (s == "Active") {
                                    driverStatus = "Yes"
                                } else if (s == "Not Active") {
                                    driverStatus = "No"
                                }
                            }
                        })
                }
                R.id.createDriverBtn -> {
                    if (checkInternet(
                            requireContext()
                        )
                    ) {
                        if (isValid()) {
                            viewModel.createDriver(
                                binding.fullName.getString(),
                                binding.mobileNumber.getString(),
                                binding.emailId.getString(),
                                binding.licenceNumber.getString(),
                                driverStatus,
                                if (driverLicenceImg.isEmpty()) null else getImageInMultiPart(
                                    driverLicenceImg,
                                    "driver_pic"
                                ),
                                if (uploadDriverImg.isEmpty()) null else getImageInMultiPart(
                                    uploadDriverImg,
                                    "upload_lice_pic"
                                )
                            )
                        }
                    }
                }

                R.id.uploadDriverCl, R.id.uploadImg -> {
                    (mActivity).mAskPermission(1, Constants.CREATE_DRIVER)
                }
                R.id.uploadDriverDocsCl, R.id.uploadDriverDocsDummy -> {
                    (mActivity).mAskPermission(2, Constants.CREATE_DRIVER)
                }
            }
        })
        viewModel.responseList.observe(requireActivity(), Observer {
            it?.let {
                if (it.status == 200 && it.message == "Driver successfully added") {
                    showSuccessDialog(
                        "Your Driver is been register with us, we will verify and come back to you soon",
                        requireContext(),
                        object : MyDialogListener {
                            override fun onResult(result: Any?) {
                                mActivity.goBack()
                            }
                        })
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

    fun setSelectedImage(it: String, isImgSelected: Int) {
        try {
            when (isImgSelected) {
                1 -> {
                    driverLicenceImg = it
                    Glide.with(this)
                        .load(it)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.placeholder)
                        .transform(FitCenter(), RoundedCorners(10))
                        .into(binding.uploadDriver)
                    binding.isDriverImage = true
                }
                2 -> {
                    uploadDriverImg = it
                    Glide.with(this)
                        .load(it)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.placeholder)
                        .transform(FitCenter(), RoundedCorners(10))
                        .into(binding.uploadDriverDocs)
                    binding.isDriverLicenceImage = true

                }
            }
        } catch (e: Exception) {
        }
    }

    private fun isValid(): Boolean {
        with(binding)
        {
            when {
                fullName.getString().isEmpty() -> {
                    view.rootView.showSnackBar("Please enter name")
                    return false
                }
                mobileNumber.getString().isEmpty() -> {
                    view.rootView.showSnackBar("Please enter mobile number")
                    return false
                }
                !mobileNumber.getString().isValidMobile -> {
                    view.rootView.showSnackBar("Please enter valid mobile number")
                    return false
                }
                licenceNumber.getString().isEmpty() -> {
                    view.rootView.showSnackBar("Please enter licence number")
                    return false
                }
                emailId.getString().isEmpty() -> {
                    view.rootView.showSnackBar("Please enter email id")
                    return false
                }
                !emailId.getString().isValidEmail -> {
                    view.rootView.showSnackBar("Please enter valid email id")
                    return false
                }
                uploadDriverImg.getString().isEmpty() -> {
                    view.rootView.showSnackBar("Please upload Driver Photo")
                    return false
                }
                driverLicenceImg.getString().isEmpty() -> {
                    view.rootView.showSnackBar("Please upload Driver's Licence Photo")
                    return false
                }
                else -> {
                    return true
                }
            }
        }
    }


}