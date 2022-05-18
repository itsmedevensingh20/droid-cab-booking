package com.cabbooking.ui.more

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cabbooking.R
import com.cabbooking.base.BaseFragment
import com.cabbooking.databinding.FragmentMoreBinding
import com.cabbooking.ui.login.LoginActivity

class MoreFragment : BaseFragment<FragmentMoreBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewModel()
    }

    override val layoutRes: Int
        get() = R.layout.fragment_more

    override fun bindViewModel() {
        binding.lifecycleOwner = this
    }

}