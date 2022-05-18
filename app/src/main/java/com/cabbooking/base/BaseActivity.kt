package com.cabbooking.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cabbooking.dialogs.CustomProgressDialog


abstract class BaseActivity< D : ViewDataBinding> : AppCompatActivity() {

    abstract val layoutRes: Int

    abstract fun bindViewModel()
    abstract fun init()
    abstract fun initControl()
    lateinit var binding: D
    private val progressDialog by lazy { CustomProgressDialog(this) }


    fun showProgress() {
        if (!progressDialog.isShowing) {
            progressDialog.show()
        }
    }

    fun hideProgress() {
        progressDialog.hide()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutRes)
//        viewModel = ViewModelProvider(this)[viewModelClass]
//        inItProgressLiveDataObserver()
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
//
//        viewModel.netWorkConnection.observe(this, Observer {
//            if (it?.isConnected != true) {
//                binding.root.showSnackBar("Internet is not connected, Please connect internet")
//            }
//        })
//    }


}