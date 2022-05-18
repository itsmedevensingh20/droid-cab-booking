package com.cabbooking.ui.more

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import changeFragment
import com.cabbooking.R
import com.cabbooking.base.BaseActivity
import com.cabbooking.constant.Constants
import com.cabbooking.constant.Constants.BOOKING_FRAGMENT
import com.cabbooking.constant.Constants.CAB_FRAGMENT
import com.cabbooking.constant.Constants.CREATE
import com.cabbooking.constant.Constants.CREATE_BOOKING
import com.cabbooking.constant.Constants.CREATE_CAB
import com.cabbooking.constant.Constants.CREATE_DRIVER
import com.cabbooking.constant.Constants.DRIVER_FRAGMENT
import com.cabbooking.constant.Constants.FILTER_FRAGMENT
import com.cabbooking.constant.Constants.PROFILE_FRAGMENT
import com.cabbooking.constant.Constants.ROUTE_FRAGMENT
import com.cabbooking.databinding.ActivityHomeVenderBinding
import com.cabbooking.di.MainFragmentFactory
import com.cabbooking.extension.goToNextScreenIntent
import com.cabbooking.extension.gotoFinish
import com.cabbooking.roomDB.viewmodel.UserViewModel
import com.cabbooking.sharedprefrences.SharedPreferencesWriter
import com.cabbooking.ui.booking.BookingFragment
import com.cabbooking.ui.cab.CabFragment
import com.cabbooking.ui.driver.DriverFragment
import com.cabbooking.ui.filter.FilterFragment
import com.cabbooking.ui.login.LoginActivity
import com.cabbooking.ui.route.RouteListFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import doGone
import doVisible
import kotlinx.android.synthetic.main.layout_bottom_sheet.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import showToast
import statusBarTransparent
import javax.inject.Inject


@AndroidEntryPoint
class HomeVenderActivity : BaseActivity<ActivityHomeVenderBinding>() {

    @Inject
    lateinit var fragmentFactory: MainFragmentFactory
    private val viewModel: MainViewModel by viewModels()
    private val roomViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        statusBarTransparent()
        supportFragmentManager.fragmentFactory = fragmentFactory
        bindViewModel()
        init()
        initControl()
    }

    private var isFABOpen: Boolean = false
    override val layoutRes: Int
        get() = R.layout.activity_home_vender

    override fun bindViewModel() {
        binding.homeVenderViewModel = viewModel
        binding.lifecycleOwner = this
    }

    override fun init() {
        try {
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.frame_home_vender) as NavHostFragment?
            val navController = navHostFragment?.navController
            val appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.navigation_booking, R.id.navigation_cab, R.id.navigation_driver
                )
            )
            if (navController != null) {
                setupActionBarWithNavController(navController, appBarConfiguration)
            }
        } catch (e: Exception) {
        }
    }

    private val sharedPreferences by lazy {
        SharedPreferencesWriter.getInstance(this)
    }

    private var bottomSheetExpanded: Boolean = false
    private lateinit var sheetBehavior: BottomSheetBehavior<View>
    override fun initControl() {
        binding.isBooking = true
        changeFragment(BookingFragment(), BOOKING_FRAGMENT, R.id.frame_home_vender)
        handleFloatingButton(BOOKING_FRAGMENT)
        inItLiveDataObserver()
        sheetBehavior = BottomSheetBehavior.from(binding.bottomSheetLayout)
        bottomSheet()
    }


    private fun handleFloatingButton(FRAGMENT: String) {
        with(binding)
        {
            when (FRAGMENT) {
                BOOKING_FRAGMENT -> {
                    createFB.setImageDrawable(
                        ContextCompat.getDrawable(
                            this@HomeVenderActivity,
                            R.drawable.plus
                        )
                    )
                    filter.doVisible()
                }
                CAB_FRAGMENT -> {
                    createFB.setImageDrawable(
                        ContextCompat.getDrawable(
                            this@HomeVenderActivity,
                            R.drawable.car
                        )
                    )
                    filter.doGone()
                }
                DRIVER_FRAGMENT -> {
                    createFB.setImageDrawable(
                        ContextCompat.getDrawable(
                            this@HomeVenderActivity,
                            R.drawable.driver
                        )
                    )
                    filter.doGone()
                }
                ROUTE_FRAGMENT -> {
                    createFB.doGone()
//                    createFB.setImageDrawable(
//                        ContextCompat.getDrawable(
//                            this@HomeVenderActivity,
//                            R.drawable.driver
//                        )
//                    )
                    filter.doGone()
                }
                else -> {
                    createFB.setImageDrawable(
                        ContextCompat.getDrawable(
                            this@HomeVenderActivity,
                            R.drawable.plus
                        )
                    )
                    filter.doVisible()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.option_logout -> {
                sharedPreferences.clearPreferenceValues()
                gotoFinish(LoginActivity::class.java)
                finishAffinity()
            }
        }
        return true
    }

    private fun bottomSheet() {
        sheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(view: View, p1: Int) {
                bottomSheetExpanded = (p1 != BottomSheetBehavior.STATE_COLLAPSED)
            }

            override fun onSlide(p0: View, p1: Float) {

            }
        })
        profile.setOnClickListener {
            goTo(PROFILE_FRAGMENT)
        }
    }

    private fun openBottomSheet() {
        if (sheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
            this.sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            binding.isSheetExpanded = true
            closeFABMenu()
        } else {
            sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            binding.isSheetExpanded = false
        }
    }

    override fun onBackPressed() {
        closeBottomSheet()
    }

    private var doubleBackToExitPressedOnce = false
    private fun closeBottomSheet() {
        if (bottomSheetExpanded) {
            sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            binding.isSheetExpanded = false
        } else {
            val bookingFragment = supportFragmentManager.findFragmentById(R.id.frame_home_vender)
            if (bookingFragment is BookingFragment) {
                if (doubleBackToExitPressedOnce) {
                    finishAffinity()
                    return
                }
                this.doubleBackToExitPressedOnce = true
                showToast(getString(R.string.error_back_press))
                lifecycleScope.launch {
                    delay(Constants.BACK_PRESS_TIME_INTERVAL)
                    doubleBackToExitPressedOnce = false

                }
            } else {
                homePage()
            }
        }
    }

    private fun homePage() {
        binding.isBooking = true
        binding.isCab = false
        binding.isDriver = false
        binding.isRoute = false
        binding.isMore = false
        changeFragment(BookingFragment(), BOOKING_FRAGMENT, R.id.frame_home_vender)
    }

    private fun closeFABMenu() {
        isFABOpen = false
    }

    fun passFilteredData(
        startingDay: Long?,
        endDay: Long?,
        type: ArrayList<String>?,
        lastOrUpcomingDate: Long?
    ) {
        isFilterVisible = true
        binding.frameFilter.doGone()
        val bookingFragment = supportFragmentManager.findFragmentById(R.id.frame_home_vender)
        if (bookingFragment is BookingFragment) {
            bookingFragment.passFilteredData(
                startingDay,
                endDay,
                type,
                lastOrUpcomingDate
            )
        }
    }


    fun removeFragment() {
        binding.frameFilter.doGone()
        val bookingFragment = supportFragmentManager.findFragmentById(R.id.frame_home_vender)
        if (bookingFragment is BookingFragment) {
            bookingFragment.passClearFilterData()
        }
    }

    private fun goTo(_CREATE: String) {
        goToNextScreenIntent(
            Intent(
                this@HomeVenderActivity,
                CreateActivity::class.java
            ).apply {
                putExtra(CREATE, _CREATE)
            })
    }


    var isFilterVisible: Boolean = false
    private fun inItLiveDataObserver() {
        viewModel.onCLick.observe(this) {
            when (it.id) {
                R.id.createFB -> {
                    when (supportFragmentManager.findFragmentById(R.id.frame_home_vender)) {
                        is BookingFragment -> {
                            goTo(CREATE_BOOKING)
                        }
                        is CabFragment -> {
                            goTo(CREATE_CAB)
                        }
                        is DriverFragment -> {
                            goTo(CREATE_DRIVER)
                        }
                    }
                }
                R.id.filter -> {
                    if (isFilterVisible) {
                        binding.frameFilter.doVisible()
                    } else {
                        changeFragment(FilterFragment(), FILTER_FRAGMENT, R.id.frameFilter)
                    }
                }
                R.id.bookingNav -> {
                    val currentFragment =
                        this.supportFragmentManager.findFragmentById(R.id.frame_home_vender)
                    if (currentFragment !is BookingFragment) {
                        homePage()
                        handleFloatingButton(BOOKING_FRAGMENT)
                    }
                }
                R.id.cabNav -> {
                    binding.isCab = true
                    binding.isDriver = false
                    binding.isRoute = false
                    binding.isMore = false
                    binding.isBooking = false
                    changeFragment(CabFragment(), CAB_FRAGMENT, R.id.frame_home_vender)
                    handleFloatingButton(CAB_FRAGMENT)
                }
                R.id.driverNav -> {
                    binding.isDriver = true
                    binding.isCab = false
                    binding.isRoute = false
                    binding.isMore = false
                    binding.isBooking = false
                    changeFragment(DriverFragment(), DRIVER_FRAGMENT, R.id.frame_home_vender)
                    handleFloatingButton(DRIVER_FRAGMENT)
                }
                R.id.routeNav -> {
                    binding.isRoute = true
                    binding.isDriver = false
                    binding.isCab = false
                    binding.isMore = false
                    binding.isBooking = false
                    changeFragment(RouteListFragment(), ROUTE_FRAGMENT, R.id.frame_home_vender)
                    handleFloatingButton(ROUTE_FRAGMENT)
                }
                R.id.moreNav -> {
                    binding.isMore = true
                    binding.isRoute = false
                    binding.isDriver = false
                    binding.isCab = false
                    binding.isBooking = false
                    changeFragment(MoreFragment(), "More Fragment", R.id.frame_home_vender)
                    openBottomSheet()
                }
            }
        }
    }
}