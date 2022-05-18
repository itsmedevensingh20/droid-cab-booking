package com.cabbooking.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import com.cabbooking.R

class CustomProgressDialog(
    private val mContext: Context,
    private val message: String = ""
) : Dialog(mContext) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.custom_progress_dialog)
        setCancelable(false)

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(window?.attributes)

        lp.width = (mContext.resources.displayMetrics.widthPixels * 0.90).toInt()
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.dimAmount = 0.5f
        window?.attributes = lp
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

//    fun setMessage(message: String) {
//        tv_message.text = message
//    }
}
