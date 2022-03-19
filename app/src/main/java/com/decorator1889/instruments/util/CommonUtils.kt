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
import java.util.ArrayList

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
            return R.drawable.ic_otorhinolaryngology
        }
        anesthesiology -> {
            return R.drawable.ic_anesthesiology
        }
        else -> return R.drawable.ic_urology
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
            return TypesBackgroundInstruments.SURGERY.bgr
        }
        neuro -> {
            return TypesBackgroundInstruments.NEURO.bgr
        }
        stomatology -> {
            return TypesBackgroundInstruments.STOMATOLOGY.bgr
        }
        gynecology -> {
            return TypesBackgroundInstruments.GYNECOLOGY.bgr
        }
        ophthalmology -> {
            return TypesBackgroundInstruments.OPHTHALMOLOGY.bgr
        }
        lor -> {
            return TypesBackgroundInstruments.LOR.bgr
        }
        urology -> {
            return TypesBackgroundInstruments.UROLOGY.bgr
        }
        anesthesiology -> {
            return TypesBackgroundInstruments.ANESTHESIOLOGY.bgr
        }
        else -> return TypesBackgroundInstruments.SURGERY.bgr
    }
}

enum class TypesBackgroundInstruments(val bgr: Int) {
    SURGERY(R.drawable.bgr_surgery),
    STOMATOLOGY(R.drawable.bgr_dentistry),
    GYNECOLOGY(R.drawable.bgr_obstetrics_gynecology),
    NEURO(R.drawable.bgr_neurosurgery),
    LOR(R.drawable.bgr_otorhinolaryngology),
    UROLOGY(R.drawable.bgr_urology),
    OPHTHALMOLOGY(R.drawable.bgr_ophthalmology),
    ANESTHESIOLOGY(R.drawable.bgr_anesthesiology)
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
            R.color.blue_405B67CA
        }
        middle -> {
            R.color.light_blue_407FC9E7
        }
        else -> {
            R.color.red_40E77D7D
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

enum class Types(val types: String) {
    SURGERY("Общая хирургия"),
    STOMATOLOGY("Стоматология"),
    GYNECOLOGY("Акушерство и гинекология"),
    NEURO("Нейрохирургия"),
    LOR("Оториноларингология"),
    UROLOGY("Урология"),
    OPHTHALMOLOGY("Офтальмология"),
    ANESTHESIOLOGY("Анестезиология")
}

enum class TypesColor(val color: Int) {
    SURGERY(R.color.surgery),
    STOMATOLOGY(R.color.stomatology),
    GYNECOLOGY(R.color.gynecology),
    NEURO(R.color.neuro),
    LOR(R.color.lor),
    UROLOGY(R.color.urology),
    OPHTHALMOLOGY(R.color.ophthalmology),
    ANESTHESIOLOGY(R.color.anesthesiology)
}

enum class TypesColor25(val color: Int) {
    SURGERY(R.color.surgery25),
    STOMATOLOGY(R.color.stomatology25),
    GYNECOLOGY(R.color.gynecology25),
    NEURO(R.color.neuro25),
    LOR(R.color.lor25),
    UROLOGY(R.color.urology25),
    OPHTHALMOLOGY(R.color.ophthalmology25),
    ANESTHESIOLOGY(R.color.anesthesiology25)
}

fun getNameMiniCategories(type: String): String {
    when (type) {
        surgery -> {
            return Types.SURGERY.types
        }
        stomatology -> {
            return Types.STOMATOLOGY.types
        }
        gynecology -> {
            return Types.GYNECOLOGY.types
        }
        neuro -> {
            return Types.NEURO.types
        }
        lor -> {
            return Types.LOR.types
        }
        urology -> {
            return Types.UROLOGY.types
        }
        ophthalmology -> {
            return Types.OPHTHALMOLOGY.types
        }
        else -> {
            return Types.ANESTHESIOLOGY.types
        }
    }
}

fun getColorMiniCategories(type: String): Int {
    when (type) {
        surgery -> {
            return TypesColor.SURGERY.color
        }
        stomatology -> {
            return TypesColor.STOMATOLOGY.color
        }
        gynecology -> {
            return TypesColor.GYNECOLOGY.color
        }
        neuro -> {
            return TypesColor.NEURO.color
        }
        lor -> {
            return TypesColor.LOR.color
        }
        urology -> {
            return TypesColor.UROLOGY.color
        }
        ophthalmology -> {
            return TypesColor.OPHTHALMOLOGY.color
        }
        else -> {
            return TypesColor.ANESTHESIOLOGY.color
        }
    }
}

fun getColor25MiniCategories(type: String): Int {
    when (type) {
        surgery -> {
            return TypesColor25.SURGERY.color
        }
        stomatology -> {
            return TypesColor25.STOMATOLOGY.color
        }
        gynecology -> {
            return TypesColor25.GYNECOLOGY.color
        }
        neuro -> {
            return TypesColor25.NEURO.color
        }
        lor -> {
            return TypesColor25.LOR.color
        }
        urology -> {
            return TypesColor25.UROLOGY.color
        }
        ophthalmology -> {
            return TypesColor25.OPHTHALMOLOGY.color
        }
        else -> {
            return TypesColor25.ANESTHESIOLOGY.color
        }
    }
}

fun getTitleToolbar(level: Long): String {
    return if (level == 1L) {
        easy
    } else if (level == 2L) {
        middle
    } else {
        hard
    }
}