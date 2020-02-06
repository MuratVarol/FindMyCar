package com.varol.findmycar.internal.popup


import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.varol.findmycar.R
import com.varol.findmycar.databinding.ViewPopupBinding
import com.varol.findmycar.internal.extension.emptyIfNull

const val WIDTH_PERCENTAGE = 0.8

class Popup(
    context: Context,
    uiModel: PopupUiModel,
    val callback: PopupCallback?
) : Dialog(context), PopupCallback {
    private lateinit var binder: ViewPopupBinding

    private val model: PopupUiModel

    init {
        model = createModelWithDefaults(uiModel)
    }

    override fun onConfirmClick() {
        dismiss()
        callback?.run {
            if (model.editableText == null) {
                onConfirmClick()
            } else {
                onConfirmClick(model.editableText.emptyIfNull())
            }
        }
    }

    override fun onCancelClick() {
        dismiss()
        callback?.onCancelClick()
    }

    override fun onDismiss() {
        callback?.onDismiss()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binder = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.view_popup,
            null,
            false
        )
        setContentView(binder.root)

        binder.let {
            it.model = model
            it.callback = this
        }

        setCanceledOnTouchOutside(model.cancelable)
        setCancelable(model.cancelable)
        window?.run {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val width =
                (Resources.getSystem().displayMetrics.widthPixels * WIDTH_PERCENTAGE).toInt()
            setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT)
            setGravity(Gravity.CENTER)
        }
    }

    private fun createModelWithDefaults(argsModel: PopupUiModel): PopupUiModel {
        val model: PopupUiModel = argsModel
        with(model) {
            if (cancelButtonText == null) {
                cancelButtonText = context.resources.getString(R.string.cancel)
            }

            if (confirmButtonText == null) {
                confirmButtonText = context.resources.getString(R.string.ok)
            }
        }

        return model
    }
}