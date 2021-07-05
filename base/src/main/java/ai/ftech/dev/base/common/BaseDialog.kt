package ai.ftech.dev.base.common

import ai.ftech.dev.base.BR
import ai.ftech.dev.base.R
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.view.animation.AnimationUtils
import android.view.animation.BounceInterpolator
import android.widget.RelativeLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

abstract class BaseDialog<BD : ViewDataBinding> : DialogFragment(), BaseView, View.OnClickListener {

    /**
     * Binding view
     */
    protected lateinit var binding: BD

    /**
     * Inflate layout root
     */
    private lateinit var myInflater: LayoutInflater

    /**
     * Annotation
     */
    private var layoutIdAnnotation: GetLayoutId? = null
    private var fullScreen: DialogFullScreen? = null
    private var bottomType: DialogBottomType? = null

    /**
     * Listener when dismiss dialog
     */
    private val dismissListener: DialogInterface.OnDismissListener? = null
    private var needDismissOnResume = false
    private val handlerClose: Handler? = null
    private val runnableClose = Runnable {
        try {
            dismiss()
        } catch (e: IllegalStateException) {
            needDismissOnResume = true
        }
    }

    init {
        val annotations = this::class.java.declaredAnnotations
        annotations.forEach {
            when (it) {
                is GetLayoutId -> {
                    layoutIdAnnotation = it
                }
                is DialogFullScreen -> {
                    fullScreen = it
                }
                is DialogBottomType -> {
                    bottomType = it
                }
            }
        }
    }

    abstract fun dismissOnBackPress(): Boolean

    abstract fun dismissByTouchOutside(): Boolean

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (!::myInflater.isInitialized) {
            myInflater = LayoutInflater.from(requireActivity())
        }
        layoutIdAnnotation?.let {
            binding = DataBindingUtil.inflate(myInflater, it.layoutId, container, false)
            binding.lifecycleOwner = viewLifecycleOwner
            binding.setVariable(BR.viewListener, this)
            onInitBinding()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        onPrepareInitView()
        super.onViewCreated(view, savedInstanceState)
        initAnimation()
        val background: View = view.findViewById(getBackgroundDialog())
        background.setOnClickListener {
            if (dismissByTouchOutside()) {
                dismissDialog(null)
            }
        }
        onInitView()
    }

    override fun onResume() {
        super.onResume()
        if (needDismissOnResume) {
            needDismissOnResume = false
            dismiss()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val layout = RelativeLayout(requireActivity())
        layout.apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
        val dialog = object : Dialog(requireContext()) {
            override fun onBackPressed() {
                if (dismissOnBackPress()) {
                    dismissDialog(null)
                }
            }
        }
        dialog.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(layout)
            val window = dialog.window
            window?.let { w ->
                w.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                val wlp = w.attributes
                fullScreen?.let {
                    if (it.isWidthEnable) {
                        wlp.width = WindowManager.LayoutParams.MATCH_PARENT
                    }
                    if (it.isWidthEnable) {
                        wlp.height = WindowManager.LayoutParams.MATCH_PARENT
                    }
                }
                bottomType?.let {
                    if (it.isEnable) {
                        wlp.gravity = Gravity.BOTTOM
                    }
                }
            }
        }
        configDialog(dialog)
        return dialog
    }

    override fun onDismiss(dialog: DialogInterface) {
        handlerClose?.removeCallbacks(runnableClose)
        super.onDismiss(dialog)
    }

    override fun dismiss() {
        super.dismiss()
        dismissListener?.onDismiss(dialog)
    }

    override fun onClick(v: View?) {
        v?.let { view ->
            onViewClick(view)
        }
    }

    /**
     * Must set id of parent view dialog is " backgroundDialog "
     * if you want change it, go to file item_default.xml to change
     * but always make sure the 2 ids are the same
     */
    protected abstract fun getBackgroundDialog(): Int

    /**
     * Root layout of dialog
     */
    open fun getRootViewGroup(): ViewGroup? {
        return null
    }

    /**
     * Show dialog
     *
     * @param fm should use childFragmentManager
     * @param tag should pass class simple name of dialog <xxx::class.java.simpleName>
     */
    fun showDialog(fm: FragmentManager, tag: String) {
        if (!this.isAdded) {
            show(fm, tag)
        }
    }

    /**
     * Dismiss dialog
     *
     * @param tag
     */
    fun dismissDialog(tag: String?) {
        if (this.isAdded) {
            dismiss()
        }
    }

    private fun configDialog(dialog: Dialog) {
        dialog.setCanceledOnTouchOutside(dismissByTouchOutside())
    }

    private fun animateDialog(viewGroup: ViewGroup) {
        bottomType?.let {
            viewGroup.startAnimation(
                AnimationUtils.loadAnimation(
                    context,
                    R.anim.anim_slide_from_bottom
                )
            )
        } ?: run {
            val set = AnimatorSet()
            val animatorX = ObjectAnimator.ofFloat(viewGroup, ViewGroup.SCALE_X, 0.7f, 1f)
            val animatorY = ObjectAnimator.ofFloat(viewGroup, ViewGroup.SCALE_Y, 0.7f, 1f)
            set.playTogether(animatorX, animatorY)
            set.interpolator = BounceInterpolator()
            set.duration = 500
            set.start()
        }
    }

    private fun initAnimation() {
        getRootViewGroup()?.let {
            animateDialog(it)
        }
    }
}
