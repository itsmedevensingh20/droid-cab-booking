package com.cabbooking.ui.booking

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cabbooking.databinding.LayoutCityStateListingBinding
import com.cabbooking.interfaces.CommonListener
import com.cabbooking.models.ResponseStateCityList
import orIntZero
import setDebounceClickListener


class PickUpDropAdapter() : RecyclerView.Adapter<PickUpDropAdapter.MyItemViewHolder>() {

    private var list: ArrayList<ResponseStateCityList> = ArrayList()
    var commonListener: CommonListener? = null
    var isValue: String? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyItemViewHolder {
        return MyItemViewHolder(
            LayoutCityStateListingBinding.inflate(
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
        data: ArrayList<ResponseStateCityList>,
        commonListener: CommonListener,
        isValue: String?
    ) {
        this.list = data
        this.isValue = isValue
        this.commonListener = commonListener
        notifyDataSetChanged()
    }

    inner class MyItemViewHolder(val binding: LayoutCityStateListingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int, list: ArrayList<ResponseStateCityList>) {
            try {
                val mResponse = list[position]
                with(binding)
                {
                    mResponse.cityName?.let {
                        cityStateTV.text = "$it / ${mResponse.stateTitle}"
                    }
                }

                itemView.setDebounceClickListener({
                    commonListener?.onListItemSelect(
                        "${mResponse.cityName} / ${mResponse.stateTitle}",
                        isValue.toString()
                    )
                })
            } catch (e: Exception) {
            }
        }
    }
}