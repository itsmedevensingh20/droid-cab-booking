package com.cabbooking.ui.otpVerification

import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.cabbooking.R
import com.cabbooking.base.BaseActivity
import com.cabbooking.constant.EXTRA_DATA
import com.cabbooking.databinding.ActivityOtpVerificationBinding
import com.cabbooking.extension.gotoFinish
import com.cabbooking.extension.hideKeyboard
import com.cabbooking.interfaces.MyCustomTextWatcher
import com.cabbooking.models.CommonResponse
import com.cabbooking.roomDB.viewmodel.UserViewModel
import com.cabbooking.ui.more.HomeVenderActivity
import dagger.hilt.android.AndroidEntryPoint
import getString
import kotlinx.coroutines.launch
import showSnackBar
import statusColor

@AndroidEntryPoint
class OtpVerificationActivity : BaseActivity<ActivityOtpVerificationBinding>() {
    //    private val viewModel by inject<OtpVerificationViewModel>()
    private val viewModel: OtpVerificationViewModel by viewModels()
    private val roomViewModel: UserViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        statusColor(R.color.white)
        bindViewModel()
        init()
        initControl()
    }

    private val mobile: String?
        get() = this.intent.getStringExtra(EXTRA_DATA)
    override val layoutRes: Int
        get() = R.layout.activity_otp_verification

    override fun bindViewModel() {
        binding.otpViewModelBinding = viewModel
        binding.lifecycleOwner = this
    }

    override fun init() {
        setUpOtpProp()
        viewModel.phoneNumber = mobile.toString()
        lifecycleScope.launch {
            startTimer()
        }
    }

    private fun setUpOtpProp() {
        with(binding)
        {
            etOtp1.addTextChangedListener(object : MyCustomTextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    s?.let {
                        if (s.isNotEmpty()) binding.etOtp2.requestFocus()
                    }
                }
            })
            etOtp2.addTextChangedListener(object : MyCustomTextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    s?.let {
                        if (s.isNotEmpty()) etOtp3.requestFocus()
                    }
                }
            })
            etOtp3.addTextChangedListener(object : MyCustomTextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    s?.let {
                        if (s.isNotEmpty()) etOtp4.requestFocus()
                    }
                }
            })
            etOtp4.addTextChangedListener(object : MyCustomTextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    s?.let {
                        if (s.isNotEmpty()) {
                            hideKeyboard()
                        }
                    }
                }
            })
        }
    }

    private fun startTimer() {
        var counter = 31
        object : CountDownTimer(31000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                counter--
                try {
                    val text = String.format("%02d", counter)
                    val time = "00:$text Sec"
                    binding.timeText.text = time
                    binding.sendAgainText.alpha = 0.5f
                } catch (e: Exception) {
                }
            }

            override fun onFinish() {
                try {
                    binding.sendAgainText.alpha = 1f
                    binding.sendAgainText.isEnabled = true
                    binding.sendAgainText.isClickable = true

                } catch (e: Exception) {
                }
            }
        }.start()
    }


    override fun initControl() {
        inItLiveDataObserver()
    }
/*
* {"message":"Login Successful","status":200,
* "data":{"name":"Deven Singh","email":"9149096520@faastr.com","phone":"9149096520",
* "avatar":"no-image.jpg","role":"vendor_admin","verified":"yes","is_active":"yes",
* "userToken":"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOi8vYXBnLmZhYXN0ci5jb20vYXBpL3YxL2xvZ2
* luL3VzZXIiLCJpYXQiOjE2Mzk1NTIwNzUsImV4cCI6MTYzOTU1NTY3NSwibmJmIjoxNjM5NTUyMDc1LCJqdGkiOiJ6ZFgxa01rMjFyWFdwWVoyIiwic3ViIjo
* 1NiwicHJ2IjoiODdlMGFmMWVmOWZkMTU4MTJmZGVjOTcxNTNhMTRlMGIwNDc1NDZhYSJ9.AcXnWdvfqFGzR_0JBOv8iHJL61SURrOXHVtJXzyPIKw"}}
* */

    private fun inItLiveDataObserver() {
        viewModel.onCLick.observe(this, Observer {
            when (it.id) {
                R.id.otpBtn -> {
                    if (isValidateOtp()) {
                        viewModel.getOtpVerify()
                    }
                }
                R.id.sendAgainText -> {
                    viewModel.getOtp()
                }
                R.id.backBtn -> {
                    super.onBackPressed()
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
            it?.let { its ->
                if (its.status == 200) {
                    try {
                        its.data?.let { it1 -> saveData(it1) }
                        gotoFinish(HomeVenderActivity::class.java)
                        finishAffinity()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
            }
        }
        viewModel.responseOTP.observe(this) {
            it.let { its ->
                if (its.status == 200) {
                    startTimer()
                    binding.otpRoot.showSnackBar("OTP resend successfully")
                }
            }
        }
    }

    private fun saveData(data: CommonResponse) {
        data.let { its ->
            if (roomViewModel.getUserData() == null) {
                roomViewModel.insertUserData(its)
            } else {
                roomViewModel.updateUserData(
                    CommonResponse(
                        userToken = data.userToken,
                        isLogIn = true
                    )
                )
            }
        }
    }


    private fun isValidateOtp(): Boolean{
        with(binding)
        {
            if (etOtp1.getString().isEmpty()
                && etOtp2.getString().isEmpty()
                && etOtp3.getString().isEmpty()
                && etOtp4.getString().isEmpty()
            ) {
                otpRoot.showSnackBar("Invalid OTP")
                return false
            }
            if (etOtp1.getString().isEmpty()) {
                otpRoot.showSnackBar("Invalid OTP")
                return false
            }
            if (etOtp2.getString().isEmpty()) {
                otpRoot.showSnackBar("Invalid OTP")
                return false
            }
            if (etOtp3.getString().isEmpty()) {
                otpRoot.showSnackBar("Invalid OTP")
                return false
            }
            if (etOtp4.getString().isEmpty()) {
                otpRoot.showSnackBar("Invalid OTP")
                return false
            }
            return true
        }
    }
}