package gongora.irvin.appmapchallenge.helper

import android.animation.ObjectAnimator
import android.animation.TimeInterpolator
import android.app.Activity
import android.content.Context
import android.util.Property
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.google.android.material.snackbar.Snackbar

object Util {

    const val URL_IMG = "http://www.weatherunlocked.com/Images/icons"

    fun snackBarMensaje(view: View, mensaje: String) {
        val mSnackbar = Snackbar.make(view, mensaje, Snackbar.LENGTH_SHORT)
        mSnackbar.setAction("Ok") { mSnackbar.dismiss() }
        mSnackbar.show()
    }

    fun hideKeyboard(activity: Activity) {
        val view = activity.currentFocus
        if (view != null) {
            val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun showKeyboard(edit: EditText, context: Context) {
        edit.requestFocus()
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    fun hideKeyboardFrom(context: Context, view: View) {
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun animate(
        view: View,
        property: Property<View, Float>,
        from: Float,
        to: Float,
        duration: Long,
        interpolator: TimeInterpolator
    ) {
        val cardAnimation = ObjectAnimator.ofFloat(view, property, from, to)
        cardAnimation.duration = duration
        cardAnimation.interpolator = interpolator
        cardAnimation.start()
    }
}