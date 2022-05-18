package com.cabbooking.ui.createAccount

import android.app.AlertDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import askPermission
import checkInternet
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.cabbooking.R
import com.cabbooking.base.BaseActivity
import com.cabbooking.constant.EXTRA_DATA
import com.cabbooking.databinding.ActivityCreateAccountBinding
import com.cabbooking.extension.goToNextScreenIntent
import com.cabbooking.interfaces.MyDialogListener
import com.cabbooking.interfaces.MySpinnerListener
import com.cabbooking.models.StateResponse
import com.cabbooking.roomDB.viewmodel.UserViewModel
import com.cabbooking.sharedprefrences.SharedPreferencesKeys
import com.cabbooking.sharedprefrences.SharedPreferencesWriter
import com.cabbooking.ui.otpVerification.OtpVerificationActivity
import com.cabbooking.utils.RealPathUtil
import com.imagepicker.FilePickUtils
import dagger.hilt.android.AndroidEntryPoint
import getImageInMultiPart
import getString
import isValidLicence
import isValidMobile
import isValidPinCode
import orIntZero
import showMenu
import showSnackBar
import statusBarTransparent

@AndroidEntryPoint
class CreateAccountActivity : BaseActivity<ActivityCreateAccountBinding>() {

    //    private val viewModel by inject<CreateAccountViewModel>()
    private val viewModel: CreateAccountViewModel by viewModels()
    private val roomViewModel: UserViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        statusBarTransparent()
        bindViewModel()
        init()
        initControl()
    }

    private val sharedPreferences by lazy {
        SharedPreferencesWriter.getInstance(this)
    }

    private var isVenderCheck: Boolean = false

    override val layoutRes: Int
        get() = R.layout.activity_create_account

    override fun bindViewModel() {
        binding.crViewModelBinding = viewModel
        binding.lifecycleOwner = this
    }

    override fun init() {
        neutralizeValue()
        setData()
    }

    private fun neutralizeValue() {
        with(binding)
        {
            isLicenceUploaded = false
            isAdharFrontUploaded = false
            isAdharBackUploaded = false
        }

    }

    private fun setData() {
        if (sharedPreferences.getBoolean(SharedPreferencesKeys.SELECTED_USER_OWNER) == true) {
            with(binding)
            {
                isVender = true
                isVenderCheck = true
                headingText.text = getString(R.string.create_account_for_vender)
            }
        } else {
            with(binding)
            {
                isVender = false
                isVenderCheck = false
                headingText.text = getString(R.string.create_account_for_driver)
            }
        }
    }

    override fun initControl() {
        if (isVenderCheck) {
            if (checkInternet(this)
            ) {
                viewModel.getAllStates()
            }
        }
        inItLiveDataObserver()
    }


    private var fullStateList: ArrayList<StateResponse> = ArrayList()
    private var fullDistrictList: ArrayList<StateResponse> = ArrayList()
    private var fullCityList: ArrayList<StateResponse> = ArrayList()
    private fun inItLiveDataObserver() {
        viewModel.onCLick.observe(this, Observer {
            when (it.id) {
                R.id.createAccountBtn -> {
                    if (checkInternet(
                            this
                        )
                    ) {
                        if (isValid()) {
                            if (isVenderCheck) {
                                viewModel.hitCreateVenderAccountApi()

                            } else {
                                /* Licence number
                                'HR06 19850034761',
                               */
                                viewModel.hitCreateDriverAccountApi(
                                    if (licenceImg.isEmpty()) null else getImageInMultiPart(
                                        licenceImg,
                                        "license_image"
                                    ),
                                    if (adharFrontImg.isEmpty()) null else getImageInMultiPart(
                                        adharFrontImg,
                                        "adharcard_front"
                                    ),
                                    (if (adharBackImg.isEmpty()) null else getImageInMultiPart(
                                        adharBackImg,
                                        "adharcard_back"
                                    ))
                                )
                            }
                        }
                    }
                }
                R.id.loginBtn, R.id.don_have_account, R.id.backBtn -> {
                    super.onBackPressed()
                }
                R.id.stateList -> {
                    val stateListString: ArrayList<String> = ArrayList()
                    val stateIdList: ArrayList<String> = ArrayList()
                    stateListString.clear()
                    stateIdList.clear()
                    for (i in 0 until fullStateList.size) {
                        stateListString.add(fullStateList[i].stateTitle.toString())
                        stateIdList.add(fullStateList[i].stateId.toString())
                    }
                    showMenu(
                        applicationContext,
                        stateListString,
                        stateIdList,
                        binding.stateList,
                        object : MySpinnerListener {
                            override fun onSelect(s: String, position: String) {
                                binding.stateList.text = s
                                viewModel.stateId = position
                                if (checkInternet(
                                        this@CreateAccountActivity
                                    )
                                ) {
                                    viewModel.getSelectedDistrict()
                                }

                            }
                        })
                }
                R.id.districtList -> {
                    if (fullDistrictList.isEmpty()) {
                        binding.signUpRoot.showSnackBar("Please select State first")
                    } else {
                        val districtListString: ArrayList<String> = ArrayList()
                        val districtIdList: ArrayList<String> = ArrayList()
                        districtListString.clear()
                        districtIdList.clear()
                        for (i in fullDistrictList.indices) {
                            districtListString.add(fullDistrictList[i].districtName.toString())
                            districtIdList.add(fullDistrictList[i].districtId.toString())
                        }
                        showMenu(
                            applicationContext,
                            districtListString,
                            districtIdList,
                            binding.districtList,
                            object : MySpinnerListener {
                                override fun onSelect(s: String, position: String) {
                                    binding.districtList.text = s
                                    viewModel.districtId = position
                                    if (checkInternet(
                                            this@CreateAccountActivity
                                        )
                                    ) {
                                        viewModel.getSelectedCity()
                                    }

                                }
                            })
                    }

                }
                R.id.cityList -> {
                    if (fullCityList.isEmpty()) {
                        binding.signUpRoot.showSnackBar("Please select district first")
                    } else {
                        val cityListString: ArrayList<String> = ArrayList()
                        val cityIdListString: ArrayList<String> = ArrayList()
                        cityListString.clear()
                        cityIdListString.clear()
                        for (i in fullCityList.indices) {
                            cityListString.add(fullCityList[i].cityName.toString())
                            cityIdListString.add(fullCityList[i].cityName.toString())
                        }
                        showMenu(
                            applicationContext,
                            cityListString,
                            cityIdListString,
                            binding.cityList,
                            object : MySpinnerListener {
                                override fun onSelect(s: String, position: String) {
                                    binding.cityList.text = s
                                }
                            })
                    }
                }
                R.id.imgLicence, R.id.uploadLicenceCl -> {
                    isImgSelected = 1
                    mAskPermission()
                }
                R.id.uploadAdharImgButton, R.id.adharFrontImgShow -> {
                    isImgSelected = 2
                    mAskPermission()
                }
                R.id.uploadAdharBackImgButton, R.id.adharBackImgShow -> {
                    isImgSelected = 3
                    mAskPermission()
                }
            }
        })

        viewModel.progress.observe(this, Observer {
            if (it) {
                showProgress()
            } else {
                hideProgress()
            }
        })

        viewModel.error.observe(this, Observer {
            binding.root.showSnackBar(it)
        })

        viewModel.response.observe(this) {
            it.let { its ->
                if (its.status == 200) {
                    goToNextScreenIntent(Intent(this, OtpVerificationActivity::class.java).apply {
                        putExtra(EXTRA_DATA, binding.mobileNumber.getString())
                    })
                }
            }
        }
        viewModel.responseVender.observe(this) {
            it.let { its ->
                if (its.status == 200) {
                    goToNextScreenIntent(Intent(this, OtpVerificationActivity::class.java).apply {
                        putExtra(EXTRA_DATA, binding.mobileNumber.getString())
                    })
                }
            }
        }
        viewModel.responseState.observe(this) {
            it.let { its ->
                fullStateList.clear()
                if (its.status == 200 && its.message == "State List") {
                    if (its.data?.size.orIntZero() > 0) {
                        for (i in 0 until its.data?.size.orIntZero()) {
                            its.data?.getOrNull(0)?.let { it1 -> fullStateList.add(it1) }
                        }
                    }
                }
            }
        }
        viewModel.responseDistrict.observe(this) {
            it.let { its ->
                fullDistrictList.clear()
                if (its.status == 200 && its.message == "State List") {
                    if (its.data?.size.orIntZero() > 0) {
                        for (i in 0 until its.data?.size.orIntZero()) {
                            its.data?.getOrNull(0)?.let { it1 -> fullDistrictList.add(it1) }
                        }
                    }
                }
            }
        }
        viewModel.responseCity.observe(this) {
            it.let { its ->
                fullCityList.clear()
                if (its.status == 200 && its.message == "City List") {
                    if (its.data?.size.orIntZero() > 0) {
                        for (i in 0 until its.data?.size.orIntZero()) {
                            its.data?.getOrNull(0)?.let { it1 -> fullCityList.add(it1) }
                        }
                    }
                }
            }
        }
    }

    private fun mAskPermission() {
        val permissionList = arrayOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        askPermission(this, permissionList, object : MyDialogListener {
            override fun onResult(result: Any?) {
                val option = arrayOf("Camera", "Gallery")
                val builder = AlertDialog.Builder(this@CreateAccountActivity)
                builder.setTitle("Pick image from")
                builder.setItems(option) { _, position ->
                    if (position == 0) {
                        openCamera()
                    } else if (position == 1) {
                        galleryClick()
                    }
                }
                builder.show()
            }
        })
    }

    private val onFileChoose = FilePickUtils.OnFileChoose { fileUri, _ ->
        val bitmap = BitmapFactory.decodeFile(fileUri)
        val imageUri = RealPathUtil.getImageUri(this, bitmap)
        val compressed = RealPathUtil.compressImageFile(imageUri, this)
        val addImageToFeed: String? = compressed.path
        addImageToFeed?.let {
            setSelectedImage(it.orEmpty())
        }
    }


    private var filePickUtils = FilePickUtils(this, onFileChoose)
    private var lifeCycleCallBackManager = filePickUtils.callBackManager

    //Gallery Click
    private fun galleryClick() {
        filePickUtils.requestImageGallery(FilePickUtils.STORAGE_PERMISSION_IMAGE, true, false)
    }

    private fun openCamera() {
        filePickUtils.requestImageCamera(FilePickUtils.CAMERA_PERMISSION, true, true);
    }


    var isImgSelected: Int = 0
    var licenceImg: String = ""
    var adharFrontImg: String = ""
    var adharBackImg: String = ""
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //If ResultCode is not Matched
        if (resultCode == RESULT_CANCELED) {
            return
        }
        if (lifeCycleCallBackManager != null) {
            lifeCycleCallBackManager.onActivityResult(requestCode, resultCode, data)
        }

//        if (resultCode == Activity.RESULT_OK && data != null) {


//            when (requestCode) {
//                Constants.CAMERA_CONSTANT -> {
//                    val bitmap: Bitmap = data.extras!!.get("data") as Bitmap
//                    val imageUri = RealPathUtil.getImageUri(this, bitmap)
//                    val compressed = RealPathUtil.compressImageFile(imageUri, this)
//                    val addImageToFeed: String? = compressed.path
//                    addImageToFeed.let {
//                        if (it != null) {
//                            setSelectedImage(it)
//                        }
//                    }
//                }
//                Constants.MatisseLib -> {
//                    data.let {
//                        val mSelected = Matisse.obtainResult(data)
//                        for (uri in mSelected) {
//                            val compressed = RealPathUtil.compressImageFile(uri, this)
//                            val addImageToFeed: String? = compressed.path
//                            addImageToFeed.let {
//                                if (it != null) {
//                                    setSelectedImage(it)
//                                }
//                            }
//                        }
//                    }
//                }

//            }

//        }
    }

    private fun setSelectedImage(it: String) {
        when (isImgSelected) {
            1 -> {
                licenceImg = it
                Glide.with(this)
                    .load(it)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.placeholder)
                    .transform(FitCenter(), RoundedCorners(10))
                    .into(binding.imgLicence)
                binding.isLicenceUploaded = true
            }
            2 -> {
                adharFrontImg = it
                Glide.with(this)
                    .load(it)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.placeholder)
                    .transform(FitCenter(), RoundedCorners(10))
                    .into(binding.adharFrontImgShow)
                binding.isAdharFrontUploaded = true

            }
            3 -> {
                adharBackImg = it
                Glide.with(this)
                    .load(it)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.placeholder)
                    .transform(FitCenter(), RoundedCorners(10))
                    .into(binding.adharBackImgShow)
                binding.isAdharBackUploaded = true
            }
        }
    }

    private fun isValid(): Boolean {
        with(binding)
        {
            when {
                isVenderCheck -> {
                    when {
                        fullName.getString().isEmpty() -> {
                            signUpRoot.showSnackBar("Please enter full name")
                            return false
                        }
                        companyName.getString().isEmpty() -> {
                            signUpRoot.showSnackBar("Please enter company name")
                            return false
                        }
                        mobileNumber.getString().isEmpty() -> {
                            signUpRoot.showSnackBar("Please enter mobile number")
                            return false
                        }
                        !mobileNumber.getString().isValidMobile -> {
                            signUpRoot.showSnackBar("Please enter valid mobile number")
                            return false
                        }
                        stateList.getString().isEmpty() -> {
                            signUpRoot.showSnackBar("Please select state")
                            return false
                        }
                        cityList.getString().isEmpty() -> {
                            signUpRoot.showSnackBar("Please select city")
                            return false
                        }
                        districtList.getString().isEmpty() -> {
                            signUpRoot.showSnackBar("Please select district")
                            return false
                        }
                        pinCode.getString().isEmpty() -> {
                            signUpRoot.showSnackBar("Please enter pin code")
                            return false
                        }
                        !pinCode.getString().isValidPinCode -> {
                            signUpRoot.showSnackBar("Please enter valid pin code")
                            return false
                        }
                        else -> {
                            return true
                        }
                    }
                }
                else -> {
                    when {
                        fullName.getString().isEmpty() -> {
                            signUpRoot.showSnackBar("Please enter full name")
                            return false
                        }
                        mobileNumber.getString().isEmpty() -> {
                            signUpRoot.showSnackBar("Please enter mobile number")
                            return false
                        }
                        !mobileNumber.getString().isValidMobile -> {
                            signUpRoot.showSnackBar("Please enter valid mobile number")
                            return false
                        }
                        licenceNumber.getString().isEmpty() -> {
                            signUpRoot.showSnackBar("Please enter licence number")
                            return false
                        }
                        !licenceNumber.getString().isValidLicence -> {
                            signUpRoot.showSnackBar("Please enter a valid licence number")
                            return false
                        }
                        licenceImg.getString().isEmpty() -> {
                            signUpRoot.showSnackBar("Please upload your licence image")
                            return false
                        }
                        adharNumber.getString().isEmpty() -> {
                            signUpRoot.showSnackBar("Please enter adhar number")
                            return false
                        }
                        adharFrontImg.getString().isEmpty() -> {
                            signUpRoot.showSnackBar("Please upload your Adhar's front side image")
                            return false
                        }
                        adharBackImg.getString().isEmpty() -> {
                            signUpRoot.showSnackBar("Please upload your Adhar's back side image")
                            return false
                        }
                        else -> {
                            return true
                        }
                    }
                }
            }
        }
    }
}