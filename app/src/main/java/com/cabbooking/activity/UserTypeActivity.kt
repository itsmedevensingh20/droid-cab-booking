package com.cabbooking.activity

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.cabbooking.R
import com.cabbooking.databinding.ActivityUserTypeBinding
import com.cabbooking.extension.goToNextScreen
import com.cabbooking.roomDB.viewmodel.UserViewModel
import com.cabbooking.sharedprefrences.SharedPreferencesKeys
import com.cabbooking.sharedprefrences.SharedPreferencesWriter
import com.cabbooking.ui.login.LoginActivity
import setDebounceClickListener
import statusColor

class UserTypeActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityUserTypeBinding

    private val sharedPreferences by lazy {
        SharedPreferencesWriter.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        statusColor(R.color.white)
        setContentView(R.layout.activity_user_type)
        init()
        initControl()
    }

    private fun init() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_type)
    }

    private fun initControl() {
        binding.continueBtn.setDebounceClickListener(this)
        binding.vender.setDebounceClickListener(this)
        binding.driver.setDebounceClickListener(this)
        binding.isVenderSelected = true
        sharedPreferences.writeBooleanInt(SharedPreferencesKeys.SELECTED_USER_OWNER, true)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.continueBtn -> {
                goToNextScreen(LoginActivity::class.java)
            }
            R.id.vender -> {
                binding.isVenderSelected = true
                sharedPreferences.writeBooleanInt(SharedPreferencesKeys.SELECTED_USER_OWNER, true)
            }
            R.id.driver -> {
                binding.isVenderSelected = false
                sharedPreferences.writeBooleanInt(SharedPreferencesKeys.SELECTED_USER_OWNER, false)
            }
        }
    }

}