package com.cabbooking.ui.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import checkInternet
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.cabbooking.R
import com.cabbooking.activity.UserTypeActivity
import com.cabbooking.base.BaseFragment
import com.cabbooking.constant.Constants
import com.cabbooking.databinding.FragmentProfileBinding
import com.cabbooking.extension.gotoFinish
import com.cabbooking.interfaces.MyDialogListener
import com.cabbooking.models.UserPersonalResponse
import com.cabbooking.roomDB.viewmodel.UserViewModel
import com.cabbooking.ui.more.CreateActivity
import dagger.hilt.android.AndroidEntryPoint
import orLongZero
import showLogoutDialog
import showSnackBar

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>() {

    private val viewModel: ProfileViewModel by viewModels()

    private val roomViewModel: UserViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewModel()
        initControl()
    }

    private val mActivity by lazy {
        activity as CreateActivity
    }

    override val layoutRes: Int
        get() = R.layout.fragment_profile

    override fun bindViewModel() {
        binding.profileViewModel = viewModel
        binding.lifecycleOwner = this
    }

    private fun initControl() {
        viewModel.accessToken =
            "${Constants.TOKEN_AUTH} ${roomViewModel.getUserData()?.userToken.orEmpty()}"
        inItLiveDataObserver()
    }

    override fun onStart() {
        super.onStart()
        apiCalling()
    }

    private fun apiCalling() {
        if (checkInternet(mActivity)) {
            viewModel.getProfileApi()
        }
    }

    private fun inItLiveDataObserver() {
        viewModel.onCLick.observe(this) {
            when (it.id) {
                R.id.logoutBtn -> {
                    showLogoutDialog(requireContext(), object : MyDialogListener {
                        override fun onResult(result: Any?) {
                            roomViewModel.deleteUserData()
                            (activity as CreateActivity).apply {
                                gotoFinish(UserTypeActivity::class.java)
                                finishAffinity()
                            }
                        }
                    })
                }
            }
        }
        viewModel.response.observe(requireActivity(), Observer {
            it?.let {
                if (it.status == 200 && it.message == "User Data") {
                    setUIData(it.data)
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

    private fun setUIData(data: UserPersonalResponse.Data?) {
        try {
            with(binding) {
                userName.text = data?.name
                userNumber.text = data?.phone
                userEmail.text = data?.email
                if (data?.balance.orLongZero() > 0) {
                    balance.text =
                        "${getString(R.string.rupees_symbol)} ${data?.balance.toString()}"
                }
                Glide.with(this@ProfileFragment)
                    .load(data?.avatar)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .apply(
                        RequestOptions().placeholder(
                            R.drawable.dzire
                        )
                    )
                    .transform(FitCenter(), RoundedCorners(5))
                    .into(profilePic)

            }
        } catch (ex: Exception) {

        }
    }

}