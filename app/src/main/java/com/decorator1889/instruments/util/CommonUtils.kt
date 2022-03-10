package com.decorator1889.instruments.util

import android.content.Context
import android.graphics.Rect
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.annotation.DimenRes
import androidx.annotation.IntegerRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.decorator1889.instruments.App
import com.decorator1889.instruments.R
import com.google.android.material.snackbar.Snackbar

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
        neurosurgery -> {
            return R.drawable.ic_neurosurgery
        }
        dentistry -> {
            return R.drawable.ic_dentistry
        }
        obstetrics_gynecology -> {
            return R.drawable.ic_obstetrics_gynecology
        }
        ophthalmology -> {
            return R.drawable.ic_ophthalmology
        }
        otorhinolaryngology -> {
            return  R.drawable.ic_otorhinolaryngology
        }
        else -> return R.drawable.ic_surgery
    }
}

fun getInstrumentsBgr(type: String): Int {
    when (type) {
        surgery -> {
            return R.drawable.bgr_surgery
        }
        neurosurgery -> {
            return R.drawable.bgr_neurosurgery
        }
        dentistry -> {
            return R.drawable.bgr_dentistry
        }
        obstetrics_gynecology -> {
            return R.drawable.bgr_obstetrics_gynecology
        }
        ophthalmology -> {
            return R.drawable.bgr_ophthalmology
        }
        otorhinolaryngology -> {
            return  R.drawable.bgr_otorhinolaryngology
        }
        else -> return R.drawable.bgr_surgery
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

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

const val dentistry = "Стоматология"
const val neurosurgery = "Нейрохирургия"
const val surgery = "Общая хирургия"
const val obstetrics_gynecology = "Акушерство и гинекология"
const val ophthalmology = "Офтальмология"
const val otorhinolaryngology = "Оториноларингология"