package com.cabbooking.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import checkInternet
import com.cabbooking.R
import com.cabbooking.base.BaseActivity
import com.cabbooking.constant.EXTRA_DATA
import com.cabbooking.databinding.ActivityLoginBinding
import com.cabbooking.extension.goToNextScreen
import com.cabbooking.extension.goToNextScreenIntent
import com.cabbooking.ui.createAccount.CreateAccountActivity
import com.cabbooking.ui.otpVerification.OtpVerificationActivity
import dagger.hilt.android.AndroidEntryPoint
import getString
import isValidMobile
import showSnackBar
import statusBarTransparent


@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>() {
    private val viewModel: LoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        statusBarTransparent()
        bindViewModel()
        init()
        initControl()
    }

    override val layoutRes: Int
        get() = R.layout.activity_login

    override fun bindViewModel() {
        binding.loginViewModelBinding = viewModel
        binding.lifecycleOwner = this
    }

    override fun init() {
        Log.e("###", "Login Screen")
    }

    override fun initControl() {
        inItLiveDataObserver()
    }

    private fun inItLiveDataObserver() {
        viewModel.onCLick.observe(this, Observer {
            when (it.id) {
                R.id.loginBtn -> {
                    if (checkInternet(this)) {
                        if (isMobileNumberValid()) {
                            viewModel.hitLogin()
                        }
                    }
                }
                R.id.don_have_account, R.id.createAccountBtn -> {
                    goToNextScreen(CreateAccountActivity::class.java)
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
        viewModel.response.observe(this, Observer {
            it.let { its ->
                if (its.status == 200) {
                    goToNextScreenIntent(
                        Intent(
                            this,
                            OtpVerificationActivity::class.java
                        ).apply {
                            putExtra(EXTRA_DATA, binding.phoneNumber.getString())
                        })

                }
            }
        })
    }

    private fun isMobileNumberValid(): Boolean {
        with(binding)
        {
            if (phoneNumber.getString().isEmpty()) {
                loginRoot.showSnackBar("Please enter mobile number")
                return false
            } else if (!phoneNumber.getString().isValidMobile) {
                loginRoot.showSnackBar("Please enter valid mobile number")
                return false
            }
        }
        return true
    }

}