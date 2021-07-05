package ai.ftech.dev.base.common

import ai.ftech.dev.base.BR
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

abstract class BaseFragment<DB : ViewDataBinding> : Fragment(), BaseView{

    companion object {
        private val TAG = BaseFragment::class.java.simpleName
    }

    /**
     * Binding view
     */
    protected lateinit var binding: DB

    /**
     * Inflate layout root
     */
    private lateinit var myInflater: LayoutInflater

    /**
     * Manager transition fragment by Fragment Manager
     */
    private lateinit var callback: OnBackPressedCallback

    /**
     * Annotation
     */
    private var layoutIdAnnotation: GetLayoutId? = null
    private var statusBarAnnotation: FragmentStatusBar? = null
    private var attachMenuToAnnotation: FragmentAttachMenuTo? = null

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
                is FragmentStatusBar -> {
                    statusBarAnnotation = it
                }
                is FragmentAttachMenuTo -> {
                    attachMenuToAnnotation = it
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onPrepareInitView()
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        if (!::myInflater.isInitialized) {
            myInflater = LayoutInflater.from(requireActivity())
        }
        layoutIdAnnotation?.let {
            binding = DataBindingUtil.inflate(myInflater, it.layoutId, container, false)
            binding.lifecycleOwner = viewLifecycleOwner
            binding.setVariable(BR.viewListener, viewClickListener)
            onInitBinding()
        }
        return binding.root
    }

    override fun onViewCreated(view: View,
                               savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isHandleBackPressByFragment()) {
            setBackPressedDispatcher()
        }
        attachMenuToAnnotation?.let {
            setHasOptionsMenu(it.isEnable)
        }
        onInitView()
        observerViewModel()
    }

    override fun onPrepareInitView() {
        statusBarAnnotation?.let { bar ->
            setStatusColor(bar.color, bar.isDarkText)
        }
    }

    open fun isHandleBackPressByFragment() = true

    /**
     * Handle button back press of device
     */
    open fun onBackPressed() {
        backScreen()
    }

    /**
     * Navigate to screen
     *
     * @param actionId R.id.xxx - action navigate in navigation graph
     * @param args pass bundle with navigation component
     */
    fun navigateTo(actionId: Int,
                   args: Bundle? = null) {
        try {
            with(findNavController()) {
                currentDestination?.getAction(actionId)?.let {
                    if (currentDestination?.id != currentDestination?.getAction(actionId)?.destinationId) {
                        navigate(actionId, args)
                    }
                }
            }
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            Log.e(TAG, "${e.message}")
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG, "${e.message}")
        }
    }

    /**
     * Back to the previous screen
     */
    fun backScreen() {
        try {
            findNavController().navigateUp()
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            Log.e(this::class.java.simpleName, "${e.message}")
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(this::class.java.simpleName, "${e.message}")
        }
    }

    /**
     * Back to one of the previous screens in the back stack
     *
     * @param actionId R.id.xxx - id of fragment want navigate to
     * @param inclusive keep back stack
     */
    fun popBackStack(actionId: Int, inclusive: Boolean = false) {
        try {
            findNavController().popBackStack(actionId, inclusive)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            Log.e(TAG, "${e.message}")
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG, "${e.message}")
        }
    }

    private fun setBackPressedDispatcher() {
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPressed()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    private fun setStatusColor(color: Int = Color.BLACK, state: Boolean = true) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val window = activity?.window
            window?.let { w ->
                w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                var newUiVisibility = w.decorView.systemUiVisibility
                newUiVisibility = if (state) {
                    //Dark Text to show up on your light status bar
                    newUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                } else {
                    //Light Text to show up on your dark status bar
                    newUiVisibility and (View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR).inv()
                }
                w.decorView.systemUiVisibility = newUiVisibility
                w.statusBarColor = color
            }
        }
    }
}