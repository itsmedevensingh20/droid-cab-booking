package com.cabbooking.ui.more

import addFragment
import android.app.AlertDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.viewModels
import askPermission
import com.cabbooking.R
import com.cabbooking.base.BaseActivity
import com.cabbooking.constant.Constants
import com.cabbooking.databinding.ActivityCreateBinding
import com.cabbooking.interfaces.MyDialogListener
import com.cabbooking.roomDB.viewmodel.UserViewModel
import com.cabbooking.ui.booking.BookingViewModel
import com.cabbooking.ui.booking.CityStateSelectionFragment
import com.cabbooking.ui.booking.CreateBookingFragment
import com.cabbooking.ui.cab.CreateCabFragment
import com.cabbooking.ui.driver.CreateDriverFragment
import com.cabbooking.ui.profile.ProfileFragment
import com.cabbooking.utils.RealPathUtil
import com.imagepicker.FilePickUtils
import dagger.hilt.android.AndroidEntryPoint
import statusBarTransparent

@AndroidEntryPoint
class CreateActivity : BaseActivity<ActivityCreateBinding>() {

    private val viewModel: BookingViewModel by viewModels()

    //    var mCabListResponse: ArrayList<CabNDriverListResponseData> = ArrayList()
//    var mDriverListResponse: ArrayList<CabNDriverListResponseData> = ArrayList()
    private val roomViewModel: UserViewModel by viewModels()

    private val whichFragment: String?
        get() = this.intent.getStringExtra(Constants.CREATE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        statusBarTransparent()
        bindViewModel()
        init()
        initControl()
    }

    override val layoutRes: Int
        get() = R.layout.activity_create

    override fun bindViewModel() {
        binding.bookingViewModel = viewModel
        binding.lifecycleOwner = this
    }

    override fun init() {
        whichFragment()
    }

    override fun initControl() {
        viewModel.accessToken =
            "${Constants.TOKEN_AUTH} ${roomViewModel.getUserData()?.userToken.orEmpty()}"
        inItLiveDataObserver()
    }

    private fun inItLiveDataObserver() {
        viewModel.onCLick.observe(this) {
            when (it.id) {
                R.id.backBtn -> {
                    super.onBackPressed()
                    finish()
                }
            }
        }
    }

    private var isImgSelected: Int = 0
    private var fragmentType: String = ""

    fun mAskPermission(isImgSelected: Int, fragmentType: String) {
        this.isImgSelected = isImgSelected
        this.fragmentType = fragmentType
        val permissionList = arrayOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        askPermission(this@CreateActivity, permissionList, object : MyDialogListener {
            override fun onResult(result: Any?) {
                val option = arrayOf("Camera", "Gallery")
                val builder = AlertDialog.Builder(this@CreateActivity)
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
            if (fragmentType == Constants.CREATE_DRIVER) {
                val createDriver = supportFragmentManager.findFragmentById(R.id.frameLayout)
                if (createDriver is CreateDriverFragment) {
                    createDriver.setSelectedImage(it, isImgSelected)
                }
            } else if (fragmentType == Constants.CREATE_CAB) {
                val createCab = supportFragmentManager.findFragmentById(R.id.frameLayout)
                if (createCab is CreateCabFragment) {
                    createCab.setSelectedImage(it, isImgSelected)
                }
            }
        }
    }

    private var filePickUtils = FilePickUtils(this, onFileChoose)
    private var lifeCycleCallBackManager = filePickUtils.callBackManager

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_CANCELED) {
            return
        }
        if (this.lifeCycleCallBackManager != null) {
            lifeCycleCallBackManager.onActivityResult(requestCode, resultCode, data)
        }
    }

    //Gallery Click
    private fun galleryClick() {
        filePickUtils.requestImageGallery(FilePickUtils.STORAGE_PERMISSION_IMAGE, true, false)
    }

    private fun openCamera() {
        filePickUtils.requestImageCamera(FilePickUtils.CAMERA_PERMISSION, true, true);
    }


    private fun whichFragment() {
        when (whichFragment) {
            Constants.CREATE_BOOKING -> {
                addFragment(CreateBookingFragment(), R.id.frameLayout)
            }
            Constants.CREATE_DRIVER -> {
                addFragment(CreateDriverFragment(), R.id.frameLayout)
            }
            Constants.CREATE_CAB -> {
                addFragment(CreateCabFragment(), R.id.frameLayout)
            }
            Constants.PROFILE_FRAGMENT -> {
                addFragment(ProfileFragment(), R.id.frameLayout)
            }
        }
    }

    fun addFragment() {
        addFragment(CityStateSelectionFragment(), R.id.frameLayout)
    }


    fun goBack() {
        super.finish()
    }
}