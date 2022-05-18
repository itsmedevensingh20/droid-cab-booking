
package com.cabbooking.interfaces

interface LongClickCallback {
    fun onLongClick(position: Int, event: Any? = null): Boolean
}
