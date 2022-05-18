package com.cabbooking.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.cabbooking.ui.booking.BookingFragment
import com.cabbooking.ui.cab.CabFragment
import com.cabbooking.ui.driver.DriverFragment
import javax.inject.Inject

class MainFragmentFactory @Inject constructor() : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (className) {
            BookingFragment::class.java.name -> BookingFragment()
            CabFragment::class.java.name -> CabFragment()
            DriverFragment::class.java.name -> DriverFragment()
            else -> super.instantiate(classLoader, className)
        }
    }
}