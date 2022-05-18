package com.cabbooking.interfaces

import java.io.Serializable
import java.util.*

interface CommonListener  {

    fun onCLick(position: Int, _id: String, itemName: String) {

    }

    fun onListItemSelect(s: String,position:String){}


//    fun passFilteredData(
//        startingDay: Long? = null,
//        endDay: Long? = null,
//        type: ArrayList<String>? = null,
//        lastOrUpcomingDate: Long? = null
//    ) {
//
//    }
}