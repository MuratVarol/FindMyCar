package com.varol.findmycar.internal.extension

import androidx.fragment.app.Fragment
import com.varol.findmycar.internal.popup.Popup
import com.varol.findmycar.internal.popup.PopupCallback
import com.varol.findmycar.internal.popup.PopupUiModel

fun Fragment.showPopup(uiModel: PopupUiModel, callback: PopupCallback? = null) {
    this.activity?.let {
        Popup(it, uiModel, callback).show()
    }
}