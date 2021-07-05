package ai.ftech.dev.base.common.transition

import android.os.Bundle
import androidx.annotation.AnimRes
import androidx.annotation.AnimatorRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

class ScreenTransitionManageImp(
    private val fragmentManager: FragmentManager,
    private val layoutContainer: Int
) : ScreenTransitionHelper {

    override fun getScreenCount() = fragmentManager.backStackEntryCount

    override fun transitionTo(
        fragment: Fragment,
        @AnimatorRes @AnimRes enter: Int,
        @AnimatorRes @AnimRes exist: Int,
        @AnimatorRes @AnimRes popEnter: Int,
        @AnimatorRes @AnimRes popExit: Int
    ) {
        try {
            val tag = fragment::class.java.simpleName
            fragmentManager.beginTransaction().apply {
                setCustomAnimations(enter, exist, popEnter, popExit)
//                replace(layoutContainer, fragment, tag)
                add(layoutContainer, fragment, tag)
                addToBackStack(tag)
                commit()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun transitionTo(
        fragment: Fragment,
        bundle: Bundle,
        @AnimatorRes @AnimRes enter: Int,
        @AnimatorRes @AnimRes exist: Int
    ) {
        try {
            val tag = fragment::class.java.simpleName
            fragment.arguments = bundle
            fragmentManager.beginTransaction().apply {
                add(layoutContainer, fragment, tag)
                addToBackStack(tag)
                commit()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun backScreen() {
        fragmentManager.popBackStack()
    }

    override fun backToActivityRoot() {
        for (i in 0..fragmentManager.backStackEntryCount) {
            fragmentManager.popBackStack()
        }
    }

    fun getCurrentFragment(): Fragment? {
        val fragments = fragmentManager.fragments
        return fragments.lastOrNull()
    }
}
