package ai.ftech.dev.base.extension

import ai.ftech.dev.base.common.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.text.SpannableString
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun <T> AppCompatActivity.observer(liveData: LiveData<T>, onChange: (T?) -> Unit) {
    liveData.observe(this, Observer(onChange))
}

fun BaseActivity<*>.openAppSetting(activity: AppCompatActivity, REQ: Int) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    val uri = Uri.fromParts("package", packageName, null)
    intent.data = uri
    activity.startActivityForResult(intent, REQ)
}

fun BaseActivity<*>.runIfNotDestroyed(task: () -> Any?) {
    if (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            !isDestroyed
        } else {
            true
        }
    ) {
        task()
    }
}

fun AppCompatActivity.runIfNotDestroyed(task: () -> Any?) {
    if (this.lifecycle.currentState != Lifecycle.State.DESTROYED) {
        task()
    }
}

fun <T> BaseFragment<*>.observer(liveData: LiveData<T>, onChange: (T?) -> Unit) {
    liveData.observe(viewLifecycleOwner, Observer(onChange))
}

fun BaseFragment<*>.runIfNotDestroyed(task: () -> Any?) {
    if (this.lifecycle.currentState != Lifecycle.State.DESTROYED) {
        task()
    }
}

fun Any.runOnMainThread(task: () -> Any?, delayMs: Long = 0L) {
    Handler(Looper.getMainLooper()).postDelayed({
        when (this) {
            is AppCompatActivity -> {
                runIfNotDestroyed { task() }
            }
            is BaseFragment<*> -> {
                runIfNotDestroyed { task() }
            }
            is BaseActivity<*> -> {
                runIfNotDestroyed { task() }
            }
            else -> {
                task()
            }
        }
    }, delayMs)
}

fun View.onDebouncedClick(
    delayBetweenClicks: Long = DEFAULT_DEBOUNCE_INTERVAL,
    click: (view: View) -> Unit
) {
    setOnClickListener(object : DebouncedOnClickListener(delayBetweenClicks) {
        override fun onDebouncedClick(v: View) = click(v)
    })
}

fun View.onDebouncedClickGlobal(
    delayBetweenClicks: Long = DEFAULT_DEBOUNCE_INTERVAL,
    click: (view: View) -> Unit
) {
    setOnClickListener(object : GlobalDebouncedOnClickListener(delayBetweenClicks) {
        override fun onDebouncedClick(v: View) = click(v)
    })
}

fun getAppString(@StringRes stringId: Int, context: Context? = BaseApplication.appInstance): String {
    return context?.getString(stringId) ?: ""
}

fun getAppSpannableString(@StringRes stringId: Int, context: Context? = BaseApplication.appInstance): SpannableString {
    return SpannableString(context?.getString(stringId))
}

fun getAppDrawable(@DrawableRes drawableId: Int, context: Context? = BaseApplication.appInstance): Drawable? {
    if (context == null) {
        return null
    }
    return ContextCompat.getDrawable(context, drawableId)
}

fun getAppDimensionPixel(@DimenRes dimenId: Int, context: Context? = BaseApplication.appInstance) =
    context?.resources?.getDimensionPixelSize(dimenId) ?: -1

fun getAppDimension(@DimenRes dimenId: Int, context: Context? = BaseApplication.appInstance) =
    context?.resources?.getDimension(dimenId) ?: -1f

fun getAppColor(@ColorRes colorRes: Int, context: Context? = BaseApplication.appInstance) =
    context?.let { ContextCompat.getColor(it, colorRes) } ?: Color.TRANSPARENT

