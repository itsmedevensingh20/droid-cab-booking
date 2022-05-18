package com.cabbooking.interfaces

import android.text.TextWatcher

interface MyCustomTextWatcher : TextWatcher {

    override fun beforeTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }
}
