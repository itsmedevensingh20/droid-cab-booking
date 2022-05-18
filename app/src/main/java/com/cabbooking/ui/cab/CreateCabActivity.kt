package com.cabbooking.ui.cab

import android.os.Bundle
import androidx.activity.viewModels
import com.cabbooking.R
import com.cabbooking.base.BaseActivity
import com.cabbooking.databinding.ActivityCreateCabBinding
import doBack
import statusColor

class CreateCabActivity : BaseActivity<ActivityCreateCabBinding>() {
    private val viewModel: CabViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        statusColor(R.color.white)
        bindViewModel()
        init()
        initControl()
    }

    override val layoutRes: Int
        get() = R.layout.activity_create_cab

    override fun bindViewModel() {
        binding.cabViewModel = viewModel
        binding.lifecycleOwner = this
    }

    override fun init() {
        binding.backBtn.doBack()
    }

    override fun initControl() {
        TODO("Not yet implemented")
    }
}