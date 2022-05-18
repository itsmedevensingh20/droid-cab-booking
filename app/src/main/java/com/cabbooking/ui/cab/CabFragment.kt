package com.cabbooking.ui.cab

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import checkInternet
import com.cabbooking.R
import com.cabbooking.base.BaseFragment
import com.cabbooking.constant.Constants
import com.cabbooking.databinding.FragmentCabBinding
import com.cabbooking.models.CabData
import com.cabbooking.roomDB.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import doGone
import doVisible
import orIntZero
import showSnackBar

@AndroidEntryPoint
class CabFragment : BaseFragment<FragmentCabBinding>() {

    private val viewModel: CabViewModel by viewModels()
    private val roomViewModel: UserViewModel by viewModels()

    var nextKey: Int = Constants.ONE

    var mCabList: ArrayList<CabData>? = ArrayList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewModel()
        initControl()
    }

    private fun initControl() {
        viewModel.accessToken =
            "${Constants.TOKEN_AUTH} ${roomViewModel.getUserData()?.userToken.orEmpty()}"
        mCabList?.clear()
        apiCalling(nextKey)
        mSetAdapter()
        inItLiveDataObserver()
    }

    override val layoutRes: Int
        get() = R.layout.fragment_cab

    override fun bindViewModel() {
        binding.cabViewModel = viewModel
        binding.lifecycleOwner = this
    }


    private fun apiCalling(page_no: Int? = null) {
        if (checkInternet(requireActivity())) {
            viewModel.hitCabListApi(page_no, Constants.TEN)
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
                            mCabList?.addAll(it)
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

    private var adp = CabListAdapter()
    private fun mSetAdapter() {
        binding.cabListRV.adapter = adp
    }

    private fun mUpdateAdapter() {
        mCabList?.let { adp.setData(it) }
    }
}