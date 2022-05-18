package com.cabbooking.databinding

import android.graphics.drawable.Drawable
import android.text.InputFilter
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableField
import androidx.recyclerview.widget.RecyclerView

@BindingAdapter("setAdapter")
fun setAdapter(
    view: RecyclerView,
    adapter: RecyclerView.Adapter<*>
) {
    view.adapter = adapter
}

//@BindingAdapter("bind:onSingleClick")
//fun View.setOnSingleClickListener(clickListener: View.OnClickListener?) {
//    clickListener?.also {
//        setOnClickListener(OnSingleClickListener(it))
//    } ?: setOnClickListener(null)
//}

@BindingAdapter("setTextColor")
fun setTextColor(view: TextView, color: Int) {
    view.setTextColor(ContextCompat.getColor(view.context, color))
}


//background = ContextCompat.getDrawable(view.context, drawable)
@BindingAdapter("setCardViewBackground")
fun setCardViewBackground(cardView: CardView, color: Int) {
    cardView.setCardBackgroundColor(ContextCompat.getColor(cardView.context, color))
}

@BindingAdapter("disableSpace")
fun disableSpace(view: EditText, filters: Array<InputFilter>) {
    view.filters = filters
}

@BindingAdapter("bind:setDrawableEnd")
fun setDrawableEnd(textView: TextView, resId: Drawable) {
    textView.setCompoundDrawablesWithIntrinsicBounds(null, null, resId, null)
}

@BindingAdapter("bind:setViewBackground")
fun setViewBackground(view: View, resId: Drawable) {
    view.background = resId
}


//@BindingAdapter("bind:getCountryCode")
//fun getCountryCode(picker: CountryCodePicker, selectedCountryCode: ObservableField<String>) {
//    picker.setOnCountryChangeListener {
//        selectedCountryCode.set(picker.selectedCountryCode)
//    }
//}

@BindingAdapter("bind:selectedItem")
fun selectedItem(spinner: Spinner, selectedItemPosition: ObservableField<Int>) {
    spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            selectedItemPosition.set(position)
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {

        }
    }
}


//@BindingAdapter("bind:loadImage")
//fun loadImage(imageView: ImageView, imagePath: String?) {
//    imageView.loadImage(imagePath, R.drawable.default_image)
//}

//@BindingAdapter("bind:loadUserImage")
//fun loadUserImage(imageView: ImageView, imagePath: String?) {
//    imageView.loadImage(imagePath, R.drawable.user_placeholder)
//}

//@BindingAdapter("bind:invitationStatus")
//fun invitationStatus(imageView: ImageView, status: String?) {
//    when(status) {
//        "1" -> imageView.setImageResource(R.drawable.hourglass_full)
//        "2" -> imageView.setImageResource(R.drawable.check_circle)
//        "3" -> imageView.setImageResource(R.drawable.block_icon)
//    }
//}


//@BindingAdapter("bind:visibleIfFileNotExist")
//fun visibleIfFileNotExist(view: View, fileName: String) {
//    val filePath = view.context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
//    val file = File(filePath, fileName)
//    if (file.exists()) {
//        view.gone()
//    } else {
//        view.visible()
//    }
//}


//@BindingAdapter("bind:visibleIfFileExist")
//fun visibleIfFileExist(view: View, fileName: String) {
//    val filePath = view.context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
//    val file = File(filePath, fileName)
//    if (file.exists()) {
//        view.visible()
//    } else {
//        view.gone()
//    }
//}


