package com.cabbooking.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.cabbooking.R
import com.cabbooking.constant.Constants
import com.cabbooking.extension.gotoFinish
import com.cabbooking.roomDB.viewmodel.UserViewModel
import com.cabbooking.ui.more.HomeVenderActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import showToast
import statusBarTransparent

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private val roomViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        statusBarTransparent()
        setContentView(R.layout.activity_splash)
        init()
    }

    private fun init() {
        try {
            lifecycleScope.launch {
                delay(Constants.SPLASH_TIME_OUT)
                if (roomViewModel.getUserData() != null) {
                    if (roomViewModel.getUserData()?.isLogIn == true) {
                        gotoFinish(HomeVenderActivity::class.java)
                    } else {
                        gotoFinish(UserTypeActivity::class.java)
                    }
                } else {
                    gotoFinish(UserTypeActivity::class.java)
                }

            }
        } catch (e: Exception) {
            showToast("Some thing went wrong, Please contact support")
        }
    }
}