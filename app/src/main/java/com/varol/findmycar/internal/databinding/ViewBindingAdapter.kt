package com.varol.findmycar.internal.databinding

import android.content.Context
import android.graphics.Color
import android.graphics.Outline
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.view.View
import android.view.ViewOutlineProvider
import android.view.animation.AnimationUtils
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.request.RequestOptions
import com.varol.findmycar.R
import com.varol.findmycar.internal.extension.toFormattedDate
import com.varol.findmycar.internal.extension.toShortDateUiString
import com.varol.findmycar.internal.view.CustomTypefaceSpan
import com.varol.findmycar.internal.view.RootConstraintLayout
import java.util.*


@BindingAdapter("visibility", "gone", requireAll = false)
fun View.setVisibility(visible: Boolean, isGone: Boolean = true) {
    visibility = if (visible) View.VISIBLE else {
        if (isGone) View.GONE else View.INVISIBLE
    }
}

@BindingAdapter("hideIfNull")
fun View.hideIfNull(value: Any?) {
    visibility = if (value == null) View.GONE else View.VISIBLE
}

@BindingAdapter("isAtTheTop")
fun View.bringToFront(value: Boolean) {
    if (value)
        ViewCompat.setTranslationZ(this, 100f)
}

@BindingAdapter("enableBlinking")
fun View.blink(value: Boolean) {
    val fadeAnim = AnimationUtils.loadAnimation(context, R.anim.fade_in)
    if (value) {
        startAnimation(fadeAnim)
    } else {
        clearAnimation()
    }
}

@BindingAdapter("setShortDateFormat", requireAll = true)
fun AppCompatTextView.setDateTextView(date: Date?) {
    text = date?.toShortDateUiString()
}

@BindingAdapter("setShortDateFormat", requireAll = true)
fun AppCompatTextView.setDateTextView(timeInMillis: Long?) {
    text = timeInMillis?.toFormattedDate()
}

private fun String.spanLastWord(context: Context): Spanned {
    val span = SpannableString(this)
    val blankIndex = this.indexOfLast { it == ' ' }
    val boldFont: Typeface? = ResourcesCompat.getFont(context, R.font.nunito_extrabold)
    span.setSpan(
        RelativeSizeSpan(0.5f),
        blankIndex,
        this.length,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    boldFont?.let {
        span.setSpan(
            CustomTypefaceSpan(boldFont),
            blankIndex,
            this.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
    return span
}

@BindingAdapter("lottieFile")
fun LottieAnimationView.setLottieFile(resource: String) {
    setAnimation(resource)
}

@BindingAdapter("hideIfNullOrEmpty")
fun View.setVisible(text: CharSequence?) {
    visibility = if (text != null && text.isNotEmpty()) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

@BindingAdapter("curveRadius")
fun curveView(view: View?, curveRadius: Float = 20f) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        view?.outlineProvider =
            object : ViewOutlineProvider() {
                override fun getOutline(view: View?, outline: Outline?) {
                    view?.let { v ->
                        outline?.setRoundRect(
                            0,
                            0,
                            v.width,
                            (v.height + curveRadius).toInt(),
                            curveRadius
                        )
                    }
                }
            }
        view?.clipToOutline = true
    }
}

@BindingAdapter("progressVisibility")
fun setProgressStatus(rootConstraintLayout: RootConstraintLayout, isVisible: Boolean) {
    if (isVisible) rootConstraintLayout.showProgress() else rootConstraintLayout.hideProgress()
}

@BindingAdapter("drawableResource")
fun AppCompatImageView.setDrawable(resource: Drawable) {
    Glide.with(this.context)
        .load(resource)
        .into(this)
}

@BindingAdapter(requireAll = false, value = ["imageUrl", "fallbackDrawable"])
fun AppCompatImageView.loadFromUrl(url: String?, fallbackDrawable: Drawable) {

    val circularProgressDrawable = CircularProgressDrawable(this.context)
    circularProgressDrawable.apply {
        strokeWidth = 10f
        centerRadius = 50f
        start()
    }

    val options: RequestOptions = RequestOptions()
        .placeholder(circularProgressDrawable)
        .error(fallbackDrawable)
        .priority(Priority.HIGH)

    url?.let {
        Glide.with(this.context)
            .load(it)
            .apply(options)
            .into(this)

    } ?: kotlin.run {
        setDrawable(fallbackDrawable)
    }
}

@BindingAdapter("regularText", "boldText")
fun setHalfBoldText(textView: AppCompatTextView, regularText: String, boldText: String) {
    val boldFont: Typeface? = ResourcesCompat.getFont(textView.context, R.font.nunito_bold)
    boldFont?.let {
        val spannableString = SpannableStringBuilder(regularText.plus(" ").plus(boldText))
        spannableString.setSpan(
            CustomTypefaceSpan(boldFont),
            regularText.length.inc(),
            spannableString.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.text = spannableString
    }
}

@BindingAdapter("setTripleColor")
fun SwipeRefreshLayout.setTripleColor(setTriple: Boolean) {
    if (setTriple) setColorSchemeColors(
        ContextCompat.getColor(context, R.color.light_orange),
        ContextCompat.getColor(context, R.color.orange),
        ContextCompat.getColor(context, R.color.dark_orange)
    )
}