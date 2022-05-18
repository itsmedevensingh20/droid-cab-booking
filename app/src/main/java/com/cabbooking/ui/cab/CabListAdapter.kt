package com.cabbooking.ui.cab

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
import com.cabbooking.databinding.LayoutCabListBinding
import com.cabbooking.models.CabData
import orIntZero

class CabListAdapter(
) : RecyclerView.Adapter<CabListAdapter.MyItemViewHolder>() {
    private var list: java.util.ArrayList<CabData> = ArrayList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyItemViewHolder {
        return MyItemViewHolder(
            LayoutCabListBinding.inflate(
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
        data: java.util.ArrayList<CabData>
    ) {
        this.list = data
        notifyDataSetChanged()
    }

    inner class MyItemViewHolder(val binding: LayoutCabListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int, list: java.util.ArrayList<CabData>) {
            val mResponse = list[position]
            with(binding)
            {
                mResponse.title?.let {
                    vehicleName.text = it
                } ?: run { "Cab Name" }

                mResponse.registrationNumber?.let {
                    vehicleNumber.text = it
                } ?: run { "Cab Number" }

                mResponse.vehicleRequest?.let {
                    vehicleRequest.text = it
                } ?: run { "Vehicle Request" }

                mResponse.rating?.let {
                    vehicleRating.text = it
                } ?: run { "Vehicle Rating" }
                if (mResponse.isActive.equals("yes", ignoreCase = true)) {
                    vehicleStatus.text = itemView.context.getString(R.string.active)
                    vehicleStatus.setTextColor(
                        ContextCompat.getColor(
                            itemView.context,
                            R.color.green_text
                        )
                    )
                } else if (mResponse.isActive.equals("no", ignoreCase = true)) {
                    vehicleStatus.text = itemView.context.getString(R.string.un_active)
                    vehicleStatus.setTextColor(
                        ContextCompat.getColor(
                            itemView.context,
                            R.color.red_text
                        )
                    )

                }
                when {
                    mResponse.vehicleRequest.equals("null", ignoreCase = true) -> {
                        vehicleRequest.text = itemView.context.getString(R.string.vehicle_request)
                        vehicleRequest.setTextColor(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.green_text
                            )
                        )
                    }
                    mResponse.vehicleRequest.equals("Approved", ignoreCase = true) -> {
                        vehicleRequest.text = itemView.context.getString(R.string.approved)
                        vehicleRequest.setTextColor(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.green_text
                            )
                        )

                    }
                    mResponse.vehicleRequest.equals("unApproved", ignoreCase = true) -> {
                        vehicleRequest.text = itemView.context.getString(R.string.un_approved)
                        vehicleRequest.setTextColor(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.color_fe2929
                            )
                        )

                    }
                    mResponse.vehicleRequest.equals("pending", ignoreCase = true) -> {
                        vehicleRequest.text = itemView.context.getString(R.string.pending)
                        vehicleRequest.setTextColor(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.color_f9c22d
                            )
                        )

                    }
                    mResponse.vehicleRequest.equals("blacklist", ignoreCase = true) -> {
                        vehicleRequest.text = itemView.context.getString(R.string.blacklist)
                        vehicleRequest.setTextColor(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.red_text
                            )
                        )

                    }
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
                    .into(carImage)

            }
        }

    }

}