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
import com.decorator1889.instruments.util.enums.*
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
        TypesCategories.SURGERY.types -> {
            return TypesIconCategories.SURGERY.icon
        }
        TypesCategories.NEURO.types -> {
            return TypesIconCategories.NEURO.icon
        }
        TypesCategories.STOMATOLOGY.types -> {
            return TypesIconCategories.STOMATOLOGY.icon
        }
        TypesCategories.GYNECOLOGY.types -> {
            return TypesIconCategories.GYNECOLOGY.icon
        }
        TypesCategories.OPHTHALMOLOGY.types -> {
            return TypesIconCategories.OPHTHALMOLOGY.icon
        }
        TypesCategories.LOR.types -> {
            return TypesIconCategories.LOR.icon
        }
        TypesCategories.ANESTHESIOLOGY.types -> {
            return TypesIconCategories.ANESTHESIOLOGY.icon
        }
        TypesCategories.SEPARATION.types -> {
            return TypesIconCategories.SEPARATION.icon
        }
        TypesCategories.CONNECTION.types -> {
            return TypesIconCategories.CONNECTION.icon
        }
        TypesCategories.SPREADING.types -> {
            return TypesIconCategories.SPREADING.icon
        }
        TypesCategories.HOLD.types -> {
            return TypesIconCategories.HOLD.icon
        }
        else -> {
            return TypesIconCategories.STABBING.icon
        }
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
        TypesCategories.SURGERY.types -> {
            return TypesBackgroundInstruments.SURGERY.bgr
        }
        TypesCategories.NEURO.types -> {
            return TypesBackgroundInstruments.NEURO.bgr
        }
        TypesCategories.STOMATOLOGY.types -> {
            return TypesBackgroundInstruments.STOMATOLOGY.bgr
        }
        TypesCategories.GYNECOLOGY.types -> {
            return TypesBackgroundInstruments.GYNECOLOGY.bgr
        }
        TypesCategories.OPHTHALMOLOGY.types -> {
            return TypesBackgroundInstruments.OPHTHALMOLOGY.bgr
        }
        TypesCategories.LOR.types -> {
            return TypesBackgroundInstruments.LOR.bgr
        }
        TypesCategories.UROLOGY.types -> {
            return TypesBackgroundInstruments.UROLOGY.bgr
        }
        TypesCategories.ANESTHESIOLOGY.types -> {
            return TypesBackgroundInstruments.ANESTHESIOLOGY.bgr
        }
        else -> return TypesBackgroundInstruments.SURGERY.bgr
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
        TypesTest.EASY.level -> {
            TypesLevelIcon.EASY.level
        }
        TypesTest.MIDDLE.level -> {
            TypesLevelIcon.MIDDLE.level
        }
        else -> {
            TypesLevelIcon.HARD.level
        }
    }
}

fun getLevelBgr(type: String): Int {
    return when (type) {
        TypesTest.EASY.level -> {
            R.color.blue_405B67CA
        }
        TypesTest.MIDDLE.level -> {
            R.color.light_blue_407FC9E7
        }
        else -> {
            R.color.red_40E77D7D
        }
    }
}

fun getColorLevel(type: String): Int {
    return when (type) {
        TypesTest.EASY.level -> {
            R.color.blue_5B67CA
        }
        TypesTest.MIDDLE.level -> {
            R.color.light_blue_7EC3DF
        }
        else -> {
            R.color.red_E77D7D
        }
    }
}

fun getArrowTestCategory(type: String): Int {
    return when (type) {
        TypesTest.EASY.level -> {
            TypesTestArrow.EASY.level
        }
        TypesTest.MIDDLE.level  -> {
            TypesTestArrow.MIDDLE.level
        }
        else -> {
            TypesTestArrow.HARD.level
        }
    }
}

//const val easy = "Легкий уровень"
//const val middle = "Средний уровень"
//const val hard = "Сложный уровень"
//
//const val surgery = "surgery"
//const val stomatology = "stomatology"
//const val gynecology = "gynecology"
//const val neuro = "neuro"
//const val lor = "lor"
//const val urology = "urology"
//const val ophthalmology = "ophthalmology"
//const val anesthesiology = "anesthesiology"
//const val separation = "separation"

fun getNameMiniCategories(type: String): String {
    when (type) {
        TypesCategories.SURGERY.types -> {
            return Types.SURGERY.types
        }
        TypesCategories.STOMATOLOGY.types -> {
            return Types.STOMATOLOGY.types
        }
        TypesCategories.GYNECOLOGY.types -> {
            return Types.GYNECOLOGY.types
        }
        TypesCategories.NEURO.types -> {
            return Types.NEURO.types
        }
        TypesCategories.LOR.types -> {
            return Types.LOR.types
        }
        TypesCategories.UROLOGY.types -> {
            return Types.UROLOGY.types
        }
        TypesCategories.OPHTHALMOLOGY.types -> {
            return Types.OPHTHALMOLOGY.types
        }
        else -> {
            return Types.ANESTHESIOLOGY.types
        }
    }
}

fun getColorMiniCategories(type: String): Int {
    when (type) {
        TypesCategories.SURGERY.types -> {
            return TypesColor.SURGERY.color
        }
        TypesCategories.STOMATOLOGY.types -> {
            return TypesColor.STOMATOLOGY.color
        }
        TypesCategories.GYNECOLOGY.types -> {
            return TypesColor.GYNECOLOGY.color
        }
        TypesCategories.NEURO.types -> {
            return TypesColor.NEURO.color
        }
        TypesCategories.LOR.types -> {
            return TypesColor.LOR.color
        }
        TypesCategories.UROLOGY.types -> {
            return TypesColor.UROLOGY.color
        }
        TypesCategories.OPHTHALMOLOGY.types -> {
            return TypesColor.OPHTHALMOLOGY.color
        }
        else -> {
            return TypesColor.ANESTHESIOLOGY.color
        }
    }
}

fun getColor25MiniCategories(type: String): Int {
    when (type) {
        TypesCategories.SURGERY.types -> {
            return TypesColor25.SURGERY.color
        }
        TypesCategories.STOMATOLOGY.types -> {
            return TypesColor25.STOMATOLOGY.color
        }
        TypesCategories.GYNECOLOGY.types -> {
            return TypesColor25.GYNECOLOGY.color
        }
        TypesCategories.NEURO.types -> {
            return TypesColor25.NEURO.color
        }
        TypesCategories.LOR.types -> {
            return TypesColor25.LOR.color
        }
        TypesCategories.UROLOGY.types -> {
            return TypesColor25.UROLOGY.color
        }
        TypesCategories.OPHTHALMOLOGY.types -> {
            return TypesColor25.OPHTHALMOLOGY.color
        }
        else -> {
            return TypesColor25.ANESTHESIOLOGY.color
        }
    }
}

fun getTitleToolbar(level: Long): String {
    return when (level) {
        1L -> {
            TypesTest.EASY.level
        }
        2L -> {
            TypesTest.MIDDLE.level
        }
        else -> {
            TypesTest.HARD.level
        }
    }
}