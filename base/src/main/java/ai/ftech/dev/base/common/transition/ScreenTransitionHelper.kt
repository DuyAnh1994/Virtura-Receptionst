package ai.ftech.dev.base.common.transition

import android.os.Bundle
import androidx.annotation.AnimRes
import androidx.annotation.AnimatorRes
import androidx.fragment.app.Fragment

interface ScreenTransitionHelper {

    fun getScreenCount() : Int

    fun transitionTo(
        fragment: Fragment,
        @AnimatorRes @AnimRes enter: Int,
        @AnimatorRes @AnimRes exist: Int,
        @AnimatorRes @AnimRes popEnter: Int,
        @AnimatorRes @AnimRes popExit: Int
        /*@AnimatorRes @AnimRes enter: Int = android.R.anim.fade_in,
        @AnimatorRes @AnimRes exist: Int = android.R.anim.fade_out,
        @AnimatorRes @AnimRes popEnter: Int = android.R.anim.fade_in,
        @AnimatorRes @AnimRes popExit: Int = android.R.anim.fade_out*/
    )

    fun transitionTo(
        fragment: Fragment,
        bundle: Bundle,
        @AnimatorRes @AnimRes enter: Int = android.R.anim.fade_in,
        @AnimatorRes @AnimRes exist: Int = android.R.anim.fade_out
    )

    fun backScreen()

    fun backToActivityRoot()
}
