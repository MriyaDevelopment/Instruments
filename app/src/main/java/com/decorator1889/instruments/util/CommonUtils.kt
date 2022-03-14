package com.decorator1889.instruments.util

import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.IBinder
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.AnimRes
import androidx.annotation.DimenRes
import androidx.annotation.IntegerRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.decorator1889.instruments.App
import com.decorator1889.instruments.R
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import com.google.android.material.snackbar.Snackbar

fun getUriForBackendImagePath(imagePath: String?): Uri {
    return Uri.parse("http://ovz2.j04713753.pqr7m.vps.myjino.ru/image/$imagePath")
}

fun ImageView.glide(url: String, @AnimRes withAnim: Int? = null) {
    Glide
        .with(this)
        .load(getUriForBackendImagePath(url))
        .transition(
            if (withAnim != null) GenericTransitionOptions.with(withAnim)
            else GenericTransitionOptions.withNoTransition()
        )
        .placeholder(getDefaultShimmer(this.context))
        .into(this)
}

fun checkSpacesOrNotEmpty(input: String): Boolean {
    return !TextUtils.isEmpty(input.trim())
}

fun hideKeyboard(token: IBinder) {
    val context = App.getInstance().applicationContext
    val imm =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS)
}

fun getDefaultShimmer(context: Context): Drawable {
    val shimmer = Shimmer.ColorHighlightBuilder().setBaseColor(
        ContextCompat.getColor(context, R.color.backgroundDefaultShimmer)
    ).setHighlightColor(
        ContextCompat.getColor(context, R.color.shimmer)
    ).setDuration(1000)
        .setBaseAlpha(1f)
        .setHighlightAlpha(1f)
        .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
        .setAutoStart(true)
        .build()
    return ShimmerDrawable().apply {
        setShimmer(shimmer)
    }
}

fun createSnackbar(
    anchorView: View,
    text: String?,
    buttonText: String? = null,
    onButtonClicked: (() -> Unit)? = null,
): Snackbar {
    val imm =
        App.getInstance().applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    if (imm.isActive) imm.hideSoftInputFromWindow(anchorView.windowToken, 0)
    val snackbar = Snackbar.make(
        anchorView,
        text.toString(),
        Snackbar.LENGTH_SHORT
    )
    val i = App.getInstance().resources.getDimensionPixelSize(R.dimen.padding70)
    snackbar.view.animate().translationY((-i).toFloat())
    snackbar.setBackgroundTint(ContextCompat.getColor(App.getInstance(), R.color.blue_5B67CA))
    snackbar.setTextColor(ContextCompat.getColor(App.getInstance(), android.R.color.white))
    snackbar.setActionTextColor(ContextCompat.getColor(App.getInstance(), android.R.color.white))
    val textView =
        snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
    textView.maxLines = 10
    if (buttonText != null) {
        snackbar.setAction(buttonText) {
            onButtonClicked?.invoke() ?: snackbar.dismiss()
        }
        snackbar.duration = Snackbar.LENGTH_INDEFINITE
    }
    return snackbar
}

fun str(@StringRes id: Int, vararg args: Any) = App.getInstance().getString(id, *args)

fun <T> MutableLiveData<T>.toLiveData(): LiveData<T> = this

fun dimen(@DimenRes id: Int): Float = App.getInstance().resources.getDimension(id)
fun resInteger(@IntegerRes id: Int) = App.getInstance().resources.getInteger(id)

fun getInstrumentsIcon(type: String): Int {
    when (type) {
        surgery -> {
            return R.drawable.ic_surgery
        }
        neuro -> {
            return R.drawable.ic_neurosurgery
        }
        stomatology -> {
            return R.drawable.ic_dentistry
        }
        gynecology -> {
            return R.drawable.ic_obstetrics_gynecology
        }
        ophthalmology -> {
            return R.drawable.ic_ophthalmology
        }
        lor -> {
            return  R.drawable.ic_otorhinolaryngology
        }
        else -> return R.drawable.ic_surgery
    }
}

class GridDecorations(
    @DimenRes sideMargins: Int? = null,
    @DimenRes elementsMargins: Int? = null,
    @DimenRes horizontalMargins: Int? = elementsMargins,
) : RecyclerView.ItemDecoration() {

    private val res = App.getInstance().resources
    private val sideMargin = res.getDimension(sideMargins ?: R.dimen.margin8).toInt()
    private val elementsMargin = res.getDimension(elementsMargins ?: R.dimen.margin12).toInt()
    private val horizontalMargin =
        res.getDimension(horizontalMargins ?: R.dimen.margin8).toInt()

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        val position = parent.getChildAdapterPosition(view)
        val rightMargin = if (position % 2 != 0) horizontalMargin else elementsMargin / 2
        val leftMargin = if (position % 2 == 0) horizontalMargin else elementsMargin / 2
        val topMargin = if (position in 0..1) sideMargin else elementsMargin / 2
        val bottomMargin =
            if (position in state.itemCount - 1 downTo state.itemCount) sideMargin else elementsMargin / 2
        outRect.set(leftMargin, topMargin, rightMargin, bottomMargin)
    }
}

fun getInstrumentsBgr(type: String): Int {
    when (type) {
        surgery -> {
            return R.drawable.bgr_surgery
        }
        neuro -> {
            return R.drawable.bgr_neurosurgery
        }
        stomatology -> {
            return R.drawable.bgr_dentistry
        }
        gynecology -> {
            return R.drawable.bgr_obstetrics_gynecology
        }
        ophthalmology -> {
            return R.drawable.bgr_ophthalmology
        }
        lor -> {
            return  R.drawable.bgr_otorhinolaryngology
        }
        else -> return R.drawable.bgr_surgery
    }
}



fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun getLevelIcon(type: String): Int {
    return when (type) {
        easy -> {
            R.drawable.ic_easy
        }
        middle -> {
            R.drawable.ic_middle
        }
        else -> {
            R.drawable.ic_hard
        }
    }
}

fun getLevelBgr(type: String): Int {
    return when (type) {
        easy -> {
            R.drawable.bgr_easy
        }
        middle -> {
            R.drawable.bgr_middle
        }
        else -> {
            R.drawable.bgr_hard
        }
    }
}

fun getColorLevel(type: String): Int {
    return when (type) {
        easy -> {
            R.color.blue_5B67CA
        }
        middle -> {
            R.color.light_blue_7EC3DF
        }
        else -> {
            R.color.red_E77D7D
        }
    }
}

fun getArrowTestCategory(type: String): Int {
    return when (type) {
        easy -> {
            R.drawable.ic_arrow_easy
        }
        middle -> {
            R.drawable.ic_arrow_middle
        }
        else -> {
            R.drawable.ic_arrow_hard
        }
    }
}

const val easy = "Легкий уровень"
const val middle = "Средний уровень"
const val hard = "Сложный уровень"

const val surgery = "surgery"
const val stomatology = "stomatology"
const val gynecology = "gynecology"
const val neuro = "neuro"
const val lor = "lor"
const val urology = "urology"
const val ophthalmology = "ophthalmology"
const val anesthesiology = "anesthesiology"