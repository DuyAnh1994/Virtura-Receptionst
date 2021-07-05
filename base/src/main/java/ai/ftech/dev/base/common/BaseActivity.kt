package ai.ftech.dev.base.common

import ai.ftech.dev.base.BR
import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.InflateException
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseActivity<DB : ViewDataBinding> : AppCompatActivity(), BaseView {

    companion object {
        private val TAG = BaseActivity::class.java.simpleName
        private const val LAYOUT_ID_DEFAULT = -1
        private const val REQUEST_PERMISSION = 1
    }

    /**90
     * Binding view
     */
    protected lateinit var binding: DB
    /**
     * Annotation
     */
    private var layoutIdAnnotation: GetLayoutId? = null
    private var onlyPortraitScreenAnnotation: ActivityOnlyPortraitScreen? = null
    private var fixSingleTaskAnnotation: ActivityFixSingleTask? = null

    /**
     * Permission
     */
    private var onAllow: (() -> Unit)? = null
    private var onDenied: (() -> Unit)? = null

    /**
     * Safe action
     */
    private var safeAction = false
    private var waitingAction: (() -> Unit)? = null

    private var viewClickListener = View.OnClickListener {
        it?.let { view ->
            onViewClick(view)
        }
    }

    init {
        val annotations = this::class.java.declaredAnnotations
        annotations.forEach {
            when (it) {
                is GetLayoutId -> {
                    layoutIdAnnotation = it
                }
                is ActivityOnlyPortraitScreen -> {
                    onlyPortraitScreenAnnotation = it
                }
                is ActivityFixSingleTask -> {
                    fixSingleTaskAnnotation = it
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        onPrepareInitView()
        onlyPortraitScreenAnnotation?.let {
            if (it.isEnable) {
                setPortraitScreen()
            }
        }
        super.onCreate(savedInstanceState)
        try {
            fixSingleTaskAnnotation?.let {
                if (it.isEnable) {
                    if (!isTaskRoot) {
                        finish()
                        return
                    }
                }
            }
            layoutIdAnnotation?.let {
                binding = DataBindingUtil.setContentView(this, it.layoutId)
                binding.lifecycleOwner = this
                binding.setVariable(BR.viewListener, viewClickListener)
                onInitBinding()
                onInitView()
                observerViewModel()
            }
        } catch (e: InflateException) {
            e.printStackTrace()
            Log.e(TAG, "${e.message}")
        } catch (e: Resources.NotFoundException) {
            e.printStackTrace()
            Log.e(TAG, "${e.message}")
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG, "${e.message}")
        }
    }

    override fun onResume() {
        super.onResume()
        safeAction = true
        if (waitingAction != null) {
            waitingAction?.invoke()
            waitingAction = null
        }
    }

    override fun onPause() {
        safeAction = false
        super.onPause()
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (checkPermission(permissions)) {
            onAllow?.invoke()
        } else {
            onDenied?.invoke()
//            openAppSetting(this, requestCode)
        }
    }

    /**
     * Handle safe action when task in background
     *
     * @param action callback safe
     */
    fun doSaveAction(action: () -> Unit) {
        if (safeAction) {
            action.invoke()
        } else {
            waitingAction = action
        }
    }

    /**
     * Check permission run time
     *
     * @param permissions list permission
     * @return Can permission allow
     */
    fun checkPermission(permissions: Array<String>): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissions.forEach {
                if (checkSelfPermission(it) == PackageManager.PERMISSION_DENIED) {
                    return false
                }
            }
        }
        return true
    }

    /**
     * Handle task when check permission
     *
     * @param permissions list permission
     * @param onAllow callback allow permission
     * @param onDenied callback denied permission
     */
    @SuppressLint("ObsoleteSdkInt")
    protected fun doRequestPermission(permissions: Array<String>,
                                      onAllow: () -> Unit = {},
                                      onDenied: () -> Unit = {}) {
        this.onAllow = onAllow
        this.onDenied = onDenied
        if (checkPermission(permissions)) {
            onAllow()
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissions, REQUEST_PERMISSION)
            }
        }
    }

    private fun setPortraitScreen() {
        try {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG, "setPortraitScreen: ${e.message}")
        }
    }
}

