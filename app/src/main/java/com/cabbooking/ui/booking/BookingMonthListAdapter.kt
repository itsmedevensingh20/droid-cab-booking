package com.cabbooking.ui.booking

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cabbooking.R
import com.cabbooking.databinding.LayoutBookingListBinding
import com.cabbooking.extension.goToNextScreen
import com.cabbooking.interfaces.CommonListener
import com.cabbooking.models.ResponseBooking
import com.cabbooking.ui.more.CreateActivity
import getMessageDateTime
import orDoubleZero
import orIntZero
import setDebounceClickListener
import showToast


class BookingMonthListAdapter() : RecyclerView.Adapter<BookingMonthListAdapter.MyItemViewHolder>() {

    private var activity: CreateActivity? = null
    private var list: java.util.ArrayList<ResponseBooking> = ArrayList()
    var commonListener: CommonListener? = null
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyItemViewHolder {
        return MyItemViewHolder(
            LayoutBookingListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size.orIntZero()
    }

    override fun onBindViewHolder(holder: MyItemViewHolder, position: Int) =
        holder.bind(position, list)

    fun setData(
        data: java.util.ArrayList<ResponseBooking>,
        commonListener: CommonListener,
        activity: CreateActivity
    ) {
        this.list = data
        this.commonListener = commonListener
        this.activity = activity
        notifyDataSetChanged()
    }

    inner class MyItemViewHolder(val binding: LayoutBookingListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int, list: ArrayList<ResponseBooking>) {
            val mResponse = list[position]
            with(binding)
            {


                callDriver.setDebounceClickListener({
                    if (mResponse.driverNumber != null) {
                        commonListener?.onCLick(position, mResponse.driverNumber.toString(), "")
                    } else {
                        showToast("Driver number is not available", itemView.context)
                    }
                })
                itemView.setDebounceClickListener({
                    activity?.goToNextScreen(BookingDetailsActivity::class.java)
                })
                mResponse.partyName?.let {
                    userName.text = it
                }
                mResponse.driverName?.let {
                    driverName.text = it
                } ?: run { "Driver Name" }
                mResponse.pickupLocation?.let {
                    pickUpLocation.text = it
                } ?: run { "Pickup Location" }
                mResponse.tripType?.let {
                    tripType.text = it
                } ?: run { "Trip type" }
                mResponse.dropLocation?.let {
                    dropLocation.text = it

                } ?: run { "Drop Location" }
                mResponse.pendingAmount.orDoubleZero().let {
                    pendingAmount.text = "${itemView.context.getString(R.string.pending)} ${
                        itemView.context.getString(R.string.rupees_symbol)
                    } $it"
                }
                mResponse.bookingAmount.orDoubleZero().let {
                    amount.text = it.toString()
                }
                mResponse.startDate?.let {
                    dateTime.text = it.getMessageDateTime()
                } ?: run {
                    "Date"
                }
                mResponse.licenseNo?.let {
                    cabNumber.text = it
                } ?: run {
                    "Cab Number"
                }
                mResponse.driverName?.let {
                    driverName.text = it
                } ?: run {
                    "Driver Name"
                }
            }
        }
    }
}