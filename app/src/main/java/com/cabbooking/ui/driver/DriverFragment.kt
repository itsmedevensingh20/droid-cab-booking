package com.cabbooking.ui.driver

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import checkInternet
import com.cabbooking.R
import com.cabbooking.base.BaseFragment
import com.cabbooking.constant.Constants
import com.cabbooking.databinding.FragmentDriverBinding
import com.cabbooking.models.DriverData
import com.cabbooking.roomDB.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import doGone
import doVisible
import orIntZero
import showSnackBar

@AndroidEntryPoint
class DriverFragment : BaseFragment<FragmentDriverBinding>() {

    private val viewModel: DriverViewModel by viewModels()
    private val roomViewModel: UserViewModel by viewModels()

    var nextKey: Int = Constants.ONE

    var mDriverList: ArrayList<DriverData>? = ArrayList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewModel()
        initControl()
    }

    override val layoutRes: Int
        get() = R.layout.fragment_driver

    override fun bindViewModel() {
        binding.driverViewModel = viewModel
        binding.lifecycleOwner = this
    }
    private fun initControl() {
        viewModel.accessToken =
            "${Constants.TOKEN_AUTH} ${roomViewModel.getUserData()?.userToken.orEmpty()}"
        mDriverList?.clear()
        apiCalling(nextKey)
        mSetAdapter()
        inItLiveDataObserver()
    }

    private fun apiCalling(page_no: Int? = null) {
        if (checkInternet(requireActivity())) {
            viewModel.hitDriverListApi(page_no, Constants.TEN)
        }
    }

    private fun inItLiveDataObserver() {
        viewModel.onCLick.observe(this, Observer {
            when (it.id) {
                R.id.loadMore -> {
                    if (nextKey.orIntZero() > Constants.ONE) {
                        apiCalling(nextKey)
                    }
                }
            }
        })

        viewModel.response.observe(this, Observer {
            it?.let { its ->
                if (its.status == 200) {
                    its.data?.let { it ->
                        if (its.data.size.orIntZero() > 0) {
                            binding.noData.doGone()
                            mDriverList?.addAll(it)
                            mUpdateAdapter()
                            if (it.size >= 10) {
                                binding.loadMore.doVisible()
                                nextKey++
                            } else {
                                nextKey = 0
                                binding.loadMore.doGone()
                            }
                        } else {
                            binding.noData.doVisible()
                        }
                    } ?: kotlin.run {
                        binding.noData.doVisible()
                    }
                }
            }
        })
        viewModel.progress.observe(requireActivity()) {
            if (it) {
                showProgress()
            } else {
                hideProgress()
            }
        }

        viewModel.error.observe(requireActivity(), Observer {
            binding.root.showSnackBar(it)
        })
    }

    private var adp = DriverListAdapter()
    private fun mSetAdapter() {
        binding.driverListRV.adapter = adp
    }

    private fun mUpdateAdapter() {
        mDriverList?.let { adp.setData(it) }
    }
}