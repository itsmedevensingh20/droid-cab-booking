package com.cabbooking.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.cabbooking.dialogs.CustomProgressDialog

abstract class BaseFragment<D : ViewDataBinding> : Fragment() {

    abstract val layoutRes: Int
    abstract fun bindViewModel()

    protected lateinit var binding: D

    private val progressDialog by lazy { CustomProgressDialog(requireContext()) }


    fun showProgress() {
        if (!progressDialog.isShowing) {
            progressDialog.show()
        }
    }

    fun hideProgress() {
        progressDialog.hide()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate<ViewDataBinding>(inflater, layoutRes, container, false) as D
        return binding.root
    }

//    private fun inItProgressLiveDataObserver() {
//        viewModel.progress.observe(this, Observer {
//            if (it) {
//                showProgress()
//            } else {
//                hideProgress()
//            }
//        })
//
//        viewModel.error.observe(this, Observer {
//            binding.root.showSnackBar(it)
//        })
//    }

}