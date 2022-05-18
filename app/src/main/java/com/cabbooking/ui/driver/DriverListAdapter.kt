package com.cabbooking.ui.driver

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.cabbooking.R
import com.cabbooking.databinding.LayoutDriverListBinding
import com.cabbooking.models.DriverData

class DriverListAdapter(
) : RecyclerView.Adapter<DriverListAdapter.MyItemViewHolder>() {

    private var list: java.util.ArrayList<DriverData> = ArrayList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyItemViewHolder {
        return MyItemViewHolder(
            LayoutDriverListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyItemViewHolder, position: Int) =
        holder.bind(position, list)

    fun setData(
        data: java.util.ArrayList<DriverData>
    ) {
        this.list = data
        notifyDataSetChanged()
    }

    inner class MyItemViewHolder(val binding: LayoutDriverListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int, list: ArrayList<DriverData>) {
            val mResponse = list[position]
            with(binding)
            {
                mResponse.name?.let {
                    driverName.text = it
                } ?: kotlin.run { "Driver Name" }

                if (mResponse.isActive.equals("yes", ignoreCase = true)) {
                    driverStatus.text = itemView.context.getString(R.string.active)
                    driverStatus.setTextColor(
                        ContextCompat.getColor(
                            itemView.context,
                            R.color.green_text
                        )
                    )
                } else if (mResponse.isActive.equals("no", ignoreCase = true)) {
                    driverStatus.text = itemView.context.getString(R.string.un_active)
                    driverStatus.setTextColor(
                        ContextCompat.getColor(
                            itemView.context,
                            R.color.red_text
                        )
                    )
                }
                Glide.with(itemView.context)
                    .load(mResponse.avatar)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .apply(
                        RequestOptions().placeholder(
                            R.drawable.dzire
                        )
                    )
                    .transform(FitCenter(), RoundedCorners(5))
                    .into(driverImage)

                if (mResponse.licenseNo != null && mResponse.licenseNo != "n/a") {
                    licenceNumber.text = mResponse.licenseNo
                } else {
                    licenceNumber.text = "Licence Number"
                }
                mResponse.phone?.let {
                    driverNumber.text = it
                } ?: kotlin.run { "Driver Number" }

            }
        }

    }

}