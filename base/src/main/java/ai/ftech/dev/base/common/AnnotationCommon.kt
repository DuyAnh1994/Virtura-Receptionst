package ai.ftech.dev.base.common

import androidx.annotation.LayoutRes

/**
 * Layout of Activity/Fragment/Dialog - R.layout.xxx
 */
annotation class GetLayoutId(
    @LayoutRes val layoutId: Int
)

// maintain after
//annotation class GetListLayoutId(
//    @LayoutRes val listLayoutId: Int
//)

/**
 * Allow orientation portrait or landscape
 */
annotation class ActivityOnlyPortraitScreen(
    val isEnable: Boolean = false
)

/**
 * Fix single task when first time install app
 */
annotation class ActivityFixSingleTask(
    val isEnable: Boolean = false
)

///**
// * Handle button back press of device
// */
//annotation class FragmentHandleBackPress(
//    val isEnable: Boolean
//)

/**
 * Change color of status bar
 */
annotation class FragmentStatusBar(
    val color: Int,
    val isDarkText: Boolean
)

/**
 * Set menu in fragment
 */
annotation class FragmentAttachMenuTo(
    val isEnable: Boolean
)

/**
 * Set full height
 * Set full width
 */
annotation class DialogFullScreen(
    val isWidthEnable: Boolean,
    val isHeightEnable: Boolean
)

/**
 * Set mode bottom dialog
 */
annotation class DialogBottomType(
    val isEnable: Boolean
)

///**
// * Set cancel dialog when click button back press of device
// */
//annotation class DialogDismissOnBackPressed(
//    val isActive: Boolean
//)

///**
// * Set cancel dialog outside
// */
//annotation class DialogCancelOnTouchOutside(
//    val isActive: Boolean
//)
